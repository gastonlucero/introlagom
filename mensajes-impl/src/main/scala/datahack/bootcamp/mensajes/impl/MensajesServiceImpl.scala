package datahack.bootcamp.mensajes.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import datahack.bootcamp.mensajes.api.{EventoDispositivoMessage, MensajesService}

class MensajesServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends MensajesService {


  override def posicion(imei: String) = ServiceCall { _ =>
    //Buscamos la entidad asociada al imei que es nuestro identificador
    val ref = persistentEntityRegistry.refFor[DispositivoEntity](imei)
    // Enviamos el comando UltimaPosicion que solo nos retorna información
    ref.ask(UltimaPosicionCommand(imei))
  }

  override def mensaje(imei: String) = ServiceCall { (request: EventoDispositivoMessage) =>
    //Buscamos la entidad con el imei que es nuestro identificador único para cada dispostivo
    val ref = persistentEntityRegistry.refFor[DispositivoEntity](imei)
    // A la entidad le enviamos un comando que contiene el evento recibido
    ref.ask(MensajeRecibidoCommand(request.evento))
  }

}
