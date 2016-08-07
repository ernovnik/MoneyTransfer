package persistence.dal


import akka.actor.{ActorSystem, Props}
import persistence.entities.{Account, AccountsTable}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.TableQuery
import utils._
import org.scalatest.{FunSuite, Suite}

trait AbstractPersistenceTest extends FunSuite {  this: Suite =>


  trait Modules extends ConfigurationModuleImpl  with PersistenceModuleTest {
  }


  trait PersistenceModuleTest extends PersistenceModule with DbModule{
    this: Configuration  =>

    private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("h2test")

    override implicit val profile: JdbcProfile = dbConfig.driver
    override implicit val db: JdbcProfile#Backend#Database = dbConfig.db


    override val accountsDal = new BaseDalImpl[AccountsTable,Account](TableQuery[AccountsTable]) {}

    val self = this

  }

}