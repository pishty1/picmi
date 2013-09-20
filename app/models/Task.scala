package models

import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.libs.json.Json

case class Task(id: Long, label: String)


object Task {

  implicit val taskWrites = new Writes[Task] {
    def writes(t: Task): JsValue = {
      Json.obj (
    	"id" -> t.id.toString,
    	"label" -> t.label
      )
    }
  }
  
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(label: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label) values ({label})").on(
        'label -> label).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id).executeUpdate()
    }
  }

  val task = {
    get[Long]("id") ~
      get[String]("label") map {
        case id ~ label => Task(id, label)
      }
  }

}