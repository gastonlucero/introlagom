package datahack.bootcamp.mensajes.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait MensajesService extends Service {

  /**
    * Example: curl http://localhost:9000/api/mensajes/imei123
    */
  def posicion(imei: String): ServiceCall[NotUsed, String]

  /**
    * Example: curl -H "Content-Type: application/json" -X POST -d '{"evento":"valor=123ABC;lat=40.447339;long=-3.669452"}' http://localhost:9000/api/mensajes/imei123
    */
  def mensaje(imei: String): ServiceCall[EventoDispositivoMessage, Done]

  override final def descriptor = {
    import Service._
    named("mensajes-api")
      .withCalls(
        pathCall("/api/mensajes/:imei", posicion _), //Get
        pathCall("/api/mensajes/:imei", mensaje _) //Post
      )
      .withAutoAcl(true)
  }
}

/**
  * Mensaje de evento
  */
case class EventoDispositivoMessage(evento: String)

object EventoDispositivoMessage {

  /**
    * Para leer / escribir JSON usando Play Json
    */
  implicit val format: Format[EventoDispositivoMessage] = Json.format[EventoDispositivoMessage]
}

