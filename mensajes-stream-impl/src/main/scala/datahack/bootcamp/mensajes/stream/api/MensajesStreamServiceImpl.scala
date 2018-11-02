package datahack.bootcamp.mensajes.stream.api

import com.lightbend.lagom.scaladsl.api.ServiceCall
import datahack.bootcamp.mensajes.api.MensajesService

import scala.concurrent.Future

/**
  * Implementacion del servicio
  *
  * En el constructor esta inyectada la interface de MensajesService, por lo que podemos realizar
  * llamadas a los metodos que expone
  */
class MensajesStreamServiceImpl(mensajesService: MensajesService) extends MensajesStreamService {


  def streaming = ServiceCall { imeis =>
    Future.successful(imeis.mapAsync(8)(mensajesService.posicion(_).invoke()))
  }
}


