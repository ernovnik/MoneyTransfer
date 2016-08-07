package rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import entities.JsonProtocol
import persistence.entities.{Account, SimpleAccount}
import scala.concurrent.{Await, Future}
import akka.http.scaladsl.model.StatusCodes._
import JsonProtocol._
import SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.duration._
import scala.util.Success

class RoutesSpec extends AbstractRestTest {

  def actorRefFactory = system
  val modules = new Modules {}
  val accounts = new AccountRoutes(modules)

  "Account Routes" should {

    "return an empty array of accounts" in {
     modules.accountsDal.findById(1) returns Future(None)

      Get("/account/1") ~> accounts.routes ~> check {
        handled shouldEqual true
        status shouldEqual NotFound
      }
    }

    "return an array with 2 accounts" in {
      modules.accountsDal.findById(1) returns Future(Some(Account(1,"account1", 100)))
      Get("/account/1") ~> accounts.routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Option[Account]].isEmpty shouldEqual false
      }
    }

    "create an account with the json in post" in {
      modules.accountsDal.insert(Account(0,"account1",150)) returns  Future(1)
      Post("/account",SimpleAccount("account1",150)) ~> accounts.routes ~> check {
        handled shouldEqual true
        status shouldEqual Created
      }
    }

    "not handle the invalid json" in {
      Post("/account","{\"name\":\"1\"}") ~> accounts.routes ~> check {
        handled shouldEqual false
      }
    }

    "not handle an empty post" in {
      Post("/account") ~> accounts.routes ~> check {
        handled shouldEqual false
      }
    }

    "transfer money from account1 to account2" in {
            modules.accountsDal.insert(Account(0,"account1",1))

      Get("/transfer/1/1/100")~> accounts.routes ~> check {
        handled shouldEqual false
      }
    }

  }

}