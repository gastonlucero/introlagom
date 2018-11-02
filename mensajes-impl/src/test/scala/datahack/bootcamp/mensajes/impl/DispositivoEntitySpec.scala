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
      val outcome1 = driver.run(MensajeRecibidoCommand("valor=1;long=-68.12;lat=-32.3434"))
      outcome1.events should contain only MensajeNuevaPosicionEvent("valor=1;long=-68.12;lat=-32.3434")
      val outcome2 = driver.run(UltimaPosicionCommand("gps"))
      outcome2.replies should contain only "Ultima Posicion gps - evento=[valor=1;long=-68.12;lat=-32.3434] es Lope de Hoyos 10"
    }

  }
}
