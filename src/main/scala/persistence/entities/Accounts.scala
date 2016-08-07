package persistence.entities

import slick.driver.H2Driver.api._

case class Account(id: Long, name: String, balance: Int) extends BaseEntity

case class SimpleAccount(name: String, balance: Int)

class AccountsTable(tag: Tag) extends BaseTable[Account](tag, "ACCOUNTS") {
  def name = column[String]("name")
  def balance = column[Int]("balance")
  def * = (id, name, balance) <> (Account.tupled, Account.unapply)
}