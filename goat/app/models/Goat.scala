package models

import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.{WSResponse, WS}

import scala.util.{Failure, Success}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current

import scala.concurrent.Future


case class Goat(name:String, color:String, id:Option[String]=None){
  def save = Goat.save(this)

}

object Goat{
  val log = Logger(getClass)
  //implicit val goatFormat= Json.format[Goat]
  implicit val goatReads= Json.reads[Goat]
  implicit val goatWrites= Json.writes[Goat]

  def save(goat:Goat): Future[(String,Boolean)] = {
    // Assert id is set
    val id = goat.id.getOrElse(throw new IllegalArgumentException("Missing ID"))

    // Save
    val respF = WS.url("http://localhost:9200/animals/goat").post(Json.toJson(goat))
    respF.map { (resp: WSResponse) =>
      log.error("Response " + resp)
      resp.status match {
        case i if i>=200 & i < 300 => id -> true
        case _ => log.error("Response " + resp) ; id -> false
      }
    }.recover {
      case e => id -> false
    }
  }
}
