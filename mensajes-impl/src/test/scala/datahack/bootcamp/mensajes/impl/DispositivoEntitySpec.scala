package datahack.bootcamp.mensajes.impl

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class DispositivoEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll {

  private val system = ActorSystem("IntrolagomEntitySpec",
    JsonSerializerRegistry.actorSystemSetupFor(IntrolagomSerializerRegistry))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withTestDriver(block: PersistentEntityTestDriver[IntrolagomCommand[_], DispositivoEvent, DispositivoState] => Unit): Unit = {
    val driver = new PersistentEntityTestDriver(system, new DispositivoEntity, "introlagom-1")
    block(driver)
    driver.getAllIssues should have size 0
  }

  "Introlagom entity" should {

    "recibir un mensaje y actualiar el estado" in withTestDriver { driver =>
      val outcome1 = driver.run(MensajeRecibidoCommand("valor=123ABC;lat=40.447339;long=-3.669452"))
      outcome1.events should contain only MensajeNuevaPosicionEvent("valor=123ABC;lat=40.447339;long=-3.669452")
      val outcome2 = driver.run(UltimaPosicionCommand("gps"))
      outcome2.replies should contain only "Ultima Posicion gps - evento=[valor=123ABC;lat=40.447339;long=-3.669452] es Lopez de Hoyos 135"
    }

  }
}
