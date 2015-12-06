package controllers

import java.util.UUID

import models.Goat
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


class Goats extends Controller {

  def save = Action.async(parse.json){ (r: Request[JsValue]) =>
    val goatO: Option[Goat] = r.body.asOpt[Goat]
    goatO match {
      case None => Future.successful(BadRequest("me prend pas pour un con c'est pas une chèvre"))
      case Some(g) => {
        val goatWithId = g.id.map{id => g}.getOrElse(g.copy(id = Some(UUID.randomUUID().toString) ))

        val clones: List[Goat] = (1 to 10).toList.map(clone(goatWithId, _))

        val goatList =  goatWithId :: clones
//
//        val content = goatList match{
//          case Nil => "empty"
//          case x::Nil => "just One"
//          case x::Goat("blanchette", _, _)::Nil  => "just Two and first name : " + x.name
//          case x::xs if goatList.size ==43 => "many"
//          case _ => "unhandled"
//        }

        val savedGoats: List[Future[(String,Boolean)]] = goatList.map(_.save)  // Goat => Future[WsReesponse]

        val eventualResponses: Future[List[(String,Boolean)]] = Future.sequence(savedGoats)

        eventualResponses.map{ (wsresponses: List[(String,Boolean)]) =>
         if(wsresponses.forall(b => b._2)){
           Ok(Json.toJson(wsresponses.toMap))
         }else{
           BadRequest(Json.toJson(wsresponses.toMap))
         }
        }
      }
    }

  }

  private def clone(goat:Goat, index:Int) =  goat.copy(name = goat.name + s"_$index", id = Some(UUID.randomUUID().toString))

}
