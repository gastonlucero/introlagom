package datahack.bootcamp.mensajes.stream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait MensajesStreamService extends Service {

  def streaming: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("mensajes-streaming")
      .withCalls(
        namedCall("streaming", streaming)
      ).withAutoAcl(true)
  }
}

