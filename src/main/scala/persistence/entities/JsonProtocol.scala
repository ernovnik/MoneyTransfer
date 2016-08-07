package entities

import persistence.entities.{Account, SimpleAccount, Transfer}
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val accountFormat = jsonFormat3(Account)
  implicit val simpleAccountFormat = jsonFormat2(SimpleAccount)
  implicit val transferFormat = jsonFormat3(Transfer)
}