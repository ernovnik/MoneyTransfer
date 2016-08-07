import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import rest.{AccountRoutes}
import utils.{ActorModuleImpl, ConfigurationModuleImpl, PersistenceModuleImpl}

object Main extends App {
  val modules = new ConfigurationModuleImpl with ActorModuleImpl with PersistenceModuleImpl
  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()
  implicit val ec = modules.system.dispatcher

  modules.accountsDal.createTable()

  val bindingFuture = Http().bindAndHandle(new AccountRoutes(modules).routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

}