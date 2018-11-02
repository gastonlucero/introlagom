package datahack.bootcamp.mensajes.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import datahack.bootcamp.mensajes.api.MensajesService
import play.api.libs.ws.ahc.AhcWSComponents

class MensajesLagomLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new IntrolagomApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new IntrolagomApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[MensajesService])
}

abstract class IntrolagomApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  // Binding del servicio
  override lazy val lagomServer = serverFor[MensajesService](wire[MensajesServiceImpl])

  // Se registra el serializador que usaremos
  override lazy val jsonSerializerRegistry = IntrolagomSerializerRegistry

  // Registro de la entidad
  persistentEntityRegistry.register(wire[DispositivoEntity])
}
