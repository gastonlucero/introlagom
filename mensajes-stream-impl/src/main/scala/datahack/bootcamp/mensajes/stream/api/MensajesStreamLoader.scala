package datahack.bootcamp.mensajes.stream.api

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import datahack.bootcamp.mensajes.api.MensajesService
import play.api.libs.ws.ahc.AhcWSComponents

class MensajesStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new MensajesStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MensajesStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[MensajesStreamService])
}

abstract class MensajesStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Binding del servicio
  override lazy val lagomServer = serverFor[MensajesStreamService](wire[MensajesStreamServiceImpl])

  //Inyectamos el serivios MensajesService para poder utilizarlo
  lazy val mensajesService = serviceClient.implement[MensajesService]
}
