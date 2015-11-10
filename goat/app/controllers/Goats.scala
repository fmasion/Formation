package controllers

import java.util.UUID

import models.Goat
import play.api.Play
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
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

        val result: Future[WSResponse] = WS.url("http://localhost:9200/animals/goat").post(Json.toJson(goatWithId))
        result.map{ wsresponse =>
          Ok(wsresponse.body)
        }.recover{
          case e => BadRequest
        }
      }
    }

  }

}


