package rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import entities.JsonProtocol
import utils.{Configuration, PersistenceModule}
import JsonProtocol._
import SprayJsonSupport._
import persistence.entities.{Account, SimpleAccount, Transfer}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class AccountRoutes(modules: Configuration with PersistenceModule) {

  private val accountGetRoute = path("account" / IntNumber) { (accId) =>
    get {
      onComplete((modules.accountsDal.findById(accId)).mapTo[Option[Account]]) {
        case Success(accountOpt) => accountOpt match {
          case Some(acc) => complete(acc)
          case None => complete(NotFound, s"The account doesn't exist")
        }
        case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
    }
  }


  def transfer(a: Int, b: Int) : Future[Int] = Future {

//    (modules.accountsDal.findById(to)).mapTo[Option[Account]]

    a/b
  }

  private val transferGetRoute = path("transfer" / IntNumber / IntNumber / IntNumber) { (from, to, amount) =>
    get {
      onComplete(transfer(from, to)) {
        case Success(value) => complete(s"The result was $value")
        case Failure(ex)    => complete((InternalServerError, s"An error occurred: ${ex.getMessage}
      }
    }
  }

  private val accountPostRoute = path("account") {
    post {
      entity(as[SimpleAccount]) { accountToInsert => onComplete((modules.accountsDal.insert(Account(0, accountToInsert.name, accountToInsert.balance)))) {
        // ignoring the number of insertedEntities because in this case it should always be one, you might check this in other cases
        case Success(insertedEntities) => complete(Created)
        case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
      }
    }
  }

  val routes: Route = accountPostRoute ~ accountGetRoute ~ transferGetRoute

}