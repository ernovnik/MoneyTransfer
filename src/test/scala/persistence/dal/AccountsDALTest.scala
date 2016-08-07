package persistence.dal

import persistence.entities.{Account}
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.junit.JUnitRunner
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout


@RunWith(classOf[JUnitRunner])
class AccountsDALTest extends FunSuite with AbstractPersistenceTest with BeforeAndAfterAll {
  implicit val timeout = Timeout(5.seconds)

  val modules = new Modules {
  }

  test("AccountsActor: Testing Accounts DAL") {
    Await.result(modules.accountsDal.createTable(), 5.seconds)
    val numberOfEntities1: Long = Await.result((modules.accountsDal.insert(Account(0, "account1", 1))), 5.seconds)
    assert(numberOfEntities1 == 1)
    val account1: Option[Account] = Await.result((modules.accountsDal.findById(1)), 5.seconds)
    assert(!account1.isEmpty && account1.get.name.compareTo("account1") == 0)
    val numberOfEntities2: Long = Await.result((modules.accountsDal.insert(Account(0, "account2", 1))), 5.seconds)
    assert(numberOfEntities2 == 2)
    val account2: Option[Account] = Await.result((modules.accountsDal.findById(2)), 5.seconds)
    assert(!account2.isEmpty && account2.get.name.compareTo("account2") == 0)
    val empty: Option[Account] = Await.result((modules.accountsDal.findById(3)), 5.seconds)
    assert(empty.isEmpty)
  }

  override def afterAll: Unit = {
    modules.db.close()
  }
}