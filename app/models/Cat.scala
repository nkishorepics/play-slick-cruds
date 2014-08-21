package models

import play.api.db.slick._


//import models.cruds.{CrudComponent, IdentifiableTable}
//import play.api.db.slick.Profile

case class Cat(id: Option[Long], name: String, color: String) extends Entity[Long]

/**
 * This Cat component contains the database representation of your
 * furry friends
 *
 * This pattern is called the cake pattern (I think it is because
 * it tastes good :P),
 *
 * Do not worry about the scary and yummy name, it is easily copyable!
 *
 * Just follow the steps
 * for each Table you want to have:
 *  1. the play.api.db.slick.Profile "self-type" (see below for an example)
 *  2. the import profile.simple._
 *
 * The reason you want to use the cake pattern here is because
 * we imagine we have multiple different databases for production
 * and tests
 */
trait CatComponent extends CrudComponent{
  this: Profile => //<- step 1: you must add this "self-type"
  import profile.simple._ //<- step 2: then import the correct Table, ... from the profile

  class CatsTable(tag: Tag) extends Table[Cat](tag, "CAT") with IdentifiableTable[Long] {
    override def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.PrimaryKey)
    def color = column[String]("color", O.NotNull)

    def * = (id.?, name, color) <> (Cat.tupled, Cat.unapply _)
  }

  class CatRepository extends Crud[CatsTable, Cat, Long] {

    override val query = TableQuery[CatsTable]

    override def withId(cat: Cat, id: Long)(implicit session: Session): Cat = cat.copy(id = Option(id))
  }

}
