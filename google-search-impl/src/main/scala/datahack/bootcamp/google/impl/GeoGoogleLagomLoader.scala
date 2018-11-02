package datahack.bootcamp.google.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire.wire
import datahack.bootcamp.google.api.GeoGoogleService
import play.api.libs.ws.ahc.AhcWSComponents

class GeoGoogleLagomLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new GeoGoogleApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new GeoGoogleApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[GeoGoogleService])
}

abstract class GeoGoogleApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Binding del servicio
  override lazy val lagomServer = serverFor[GeoGoogleService](wire[GeoGoogleServiceImpl])

}
