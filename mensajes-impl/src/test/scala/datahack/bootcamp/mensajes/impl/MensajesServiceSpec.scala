package datahack.bootcamp.mensajes.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import datahack.bootcamp.mensajes.api.{EventoDispositivoMessage, MensajesService}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class MensajesServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new IntrolagomApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[MensajesService]

  override protected def afterAll() = server.stop()

  "Introlagom service" should {

    "ultima posicion" in {
      client.posicion("gps").invoke().map { answer =>
        assert(answer.contains("evento=[S/I]"))
      }
    }

    "allow responding with a custom message" in {
      for {
        future <- client.mensaje("gps1").invoke(EventoDispositivoMessage("valor=123ABC;lat=40.447339;long=-3.669452"))
        answer <- client.posicion("gps1").invoke()
      } yield {answer shouldBe "Ultima Posicion gps1 - evento=[valor=123ABC;lat=40.447339;long=-3.669452] es Lopez de Hoyos 135"
      }
    }
  }
}
