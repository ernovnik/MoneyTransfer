package utils

import akka.actor.{ActorPath, ActorRef, ActorSelection, Props}
import persistence.dal._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import persistence.entities.{Account, AccountsTable}
import slick.lifted.TableQuery


trait Profile {
	val profile: JdbcProfile
}


trait DbModule extends Profile{
	val db: JdbcProfile#Backend#Database
}

trait PersistenceModule {
	val accountsDal: BaseDal[AccountsTable, Account]
}


trait PersistenceModuleImpl extends PersistenceModule with DbModule{
	this: Configuration  =>

	private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("h2db")

	override implicit val profile: JdbcProfile = dbConfig.driver
	override implicit val db: JdbcProfile#Backend#Database = dbConfig.db

  override val accountsDal = new BaseDalImpl[AccountsTable, Account](TableQuery[AccountsTable]) {}

	val self = this

}
