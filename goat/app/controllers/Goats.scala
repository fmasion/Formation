package controllers

import java.util.UUID

import models.Goat
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current

import scala.concurrent.Future

/**
 * Created by fred on 04/11/2015.
 */
class Goats extends Controller {

  def save = Action.async(parse.json){ (r: Request[JsValue]) =>
    val goatO: Option[Goat] = r.body.asOpt[Goat]
    goatO match {
      case None => Future.successful(BadRequest("me prend pas pour un con c'est pas une chèvre"))
      case Some(g) => {
        val goatWithId = g.id.map{id => g}.getOrElse(g.copy(id = Some(UUID.randomUUID().toString) ))
        val goat2 = goatWithId.copy(name = goatWithId.name + "_2")

        val result: Future[WSResponse] = WS.url("http://localhost:9200/animals/goat").post(Json.toJson(goatWithId))
        val result2: Future[WSResponse] = WS.url("http://localhost:9200/animals/goat").post(Json.toJson(goat2))

        val eventualResponses: Future[Seq[WSResponse]] = Future.sequence(Seq(result, result2))

        eventualResponses.map{ (wsresponses: Seq[WSResponse]) =>
          val bodies = wsresponses.map(_.body)
          val reponseString = bodies.mkString(", ")
          Ok(reponseString)
        }
      }
    }

  }

}
