package datahack.bootcamp.google.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait GeoGoogleService extends Service {

  def direccion(latlong: String): ServiceCall[NotUsed, String]

  override final def descriptor: Descriptor = {
    import Service._
    named("geo-google")
      .withCalls(
        restCall(Method.GET, "/geo/geogoogle?latlong", direccion _)
      )
      .withAutoAcl(true)
  }
}
