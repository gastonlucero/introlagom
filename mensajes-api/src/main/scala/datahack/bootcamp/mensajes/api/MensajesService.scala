package datahack.bootcamp.mensajes.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object MensajesService {
  val TOPIC_NAME = "posiciones"
}

/**
  * The Introlagok service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the IntrolagokService.
  */
trait MensajesService extends Service {

  /**
    * Example: curl http://localhost:9000/api/mensajes/"evento"
    */
  def posicion(imei: String): ServiceCall[NotUsed, String]

  /**
    * Example: curl -H "Content-Type: application/json" -X POST -d '{"evento":"Hi"}' http://localhost:9000/api/hello/Alice
    */
  def mensaje(imei: String): ServiceCall[EventoDispositivoMessage, Done]

  override final def descriptor = {
    import Service._
    named("mensajes-api")
      .withCalls(
        pathCall("/api/mensajes/:imei", posicion _),
        pathCall("/api/mensajes/:imei", mensaje _)
      )
      .withAutoAcl(true)
  }
}

/**
  * The greeting message class.
  */
case class EventoDispositivoMessage(evento: String)

object EventoDispositivoMessage {

  /**
    * Para leer / escribir JSON usando Play Json
    */
  implicit val format: Format[EventoDispositivoMessage] = Json.format[EventoDispositivoMessage]
}


/**
  * The greeting message class used by the topic stream.
  * Different than [[EventoDispositivoMessage]], this message includes the name (id).
  */
case class GreetingMessageChanged(name: String, message: String)

object GreetingMessageChanged {
  /**
    * Format for converting greeting messages to and from JSON.
    *
    * This will be picked up by a Lagom implicit conversion from Play's JSON format to Lagom's message serializer.
    */
  implicit val format: Format[GreetingMessageChanged] = Json.format[GreetingMessageChanged]
}
