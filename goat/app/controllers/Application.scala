package controllers

import models.Goat
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

class Application extends Controller {

  def index = Action {

    val toto: Double = Play.current.configuration.getDouble("toto").getOrElse(10000)
    Ok(views.html.index( s"Your new application is ready. ${toto}"))
  }

  def user1(id:String) = Action {
    val blanchette = Goat(name = "blanchette1", color = id)
    Ok(Json.toJson(blanchette))
  }


  def user3(id:String) = Action {
    val blanchette = Goat("blanchette3", id)
    Ok(Json.toJson(blanchette))
  }


  def user2(id:Int) = Action {
    val blanchette = Goat("blanchette2", ""+id)
    Ok(Json.toJson(blanchette))
  }

}
