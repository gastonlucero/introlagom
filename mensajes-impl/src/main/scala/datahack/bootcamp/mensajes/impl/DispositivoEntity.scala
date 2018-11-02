package datahack.bootcamp.mensajes.impl

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq

class DispositivoEntity extends PersistentEntity {

  override type Command = IntrolagomCommand[_]
  override type Event = DispositivoEvent
  override type State = DispositivoState

  /**
    * Estado inicial de la entidad
    */
  override def initialState: DispositivoState = DispositivoState("S/I", LocalDateTime.now.toString)

  /**
    * Definimos los distintos comportameientos que maneja la entidad
    *
    */
  override def behavior: Behavior = {
    case DispositivoState(mensaje, _) => Actions().onCommand[MensajeRecibidoCommand, Done] {
      // Command handler for the UseGreetingMessage command
      case (MensajeRecibidoCommand(newMessage), ctx, state) =>
        // Como respuesta al comando,lo  persistimos y enviamos el evento
        //MensajeNuevaPosicionEvent que actaulizar el state de la entidad
        ctx.thenPersist(
          MensajeNuevaPosicionEvent(newMessage)
        ) { _ =>
          // Cuando el evento es persitido, se responde con Done
          ctx.reply(Done)
        }

    }.onReadOnlyCommand[UltimaPosicionCommand, String] { //QueryCommand
      // Command handler
      case (UltimaPosicionCommand(imei), ctx, state) =>
        // Respondemos con la ultima posicion del imei
        ctx.reply(s"Ultima Posicion $imei - evento=[${state.message}] es Lope de Hoyos 10")
    }.onEvent {
      // Event handler
      case (MensajeNuevaPosicionEvent(newMessage), state) =>
        // Cambiamos el estado
        DispositivoState(newMessage, LocalDateTime.now().toString)
    }
  }
}

/**
  * Representa el estado acutal de una entidad
  */
case class DispositivoState(message: String, timestamp: String)

object DispositivoState {
  implicit val format: Format[DispositivoState] = Json.format
}

/**
  * Interface para la persistencia de la entidad
  */
sealed trait DispositivoEvent extends AggregateEvent[DispositivoEvent] {
  def aggregateTag = DispositivoEvent.Tag
}

object DispositivoEvent {
  val Tag = AggregateEventTag[DispositivoEvent]
}

/**
  * Event que representa un nuevo mensaje de un dispositivo
  */
case class MensajeNuevaPosicionEvent(message: String) extends DispositivoEvent

object MensajeNuevaPosicionEvent {

  implicit val format: Format[MensajeNuevaPosicionEvent] = Json.format
}

/**
  * Interface para usar en todos los comandos que soporta la entidad
  */
sealed trait IntrolagomCommand[R] extends ReplyType[R]


/**
  * Comando para recibir mensajes
  *
  * La respuesta es un Done cuandi el comando sea persistido
  */
case class MensajeRecibidoCommand(message: String) extends IntrolagomCommand[Done]

object MensajeRecibidoCommand {
  implicit val format: Format[MensajeRecibidoCommand] = Json.format
}

/**
  * Comando para obtener la ultima posicion de un imei
  *
  * La respuesta es un String y contiene la ultima posicion del imei
  */
case class UltimaPosicionCommand(imei: String) extends IntrolagomCommand[String]

object UltimaPosicionCommand {
  implicit val format: Format[UltimaPosicionCommand] = Json.format
}

/**
  * Serializadores para todos los mensajes que maneja Lagom
  */
object IntrolagomSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[MensajeRecibidoCommand],
    JsonSerializer[UltimaPosicionCommand],
    JsonSerializer[MensajeNuevaPosicionEvent],
    JsonSerializer[DispositivoState]
  )
}
