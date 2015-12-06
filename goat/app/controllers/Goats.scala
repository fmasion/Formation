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
        val maybeNumber: Future[String] = Future("asdf")
        val x: Future[String] = maybeNumber.flatMap(x => computeHeavy(x))

        val ps: List[Int] = List(4,2,5)
        val pps: List[Int] = ps.map(p => p * 2)
        val pps2: List[Int] = ps.flatMap(p => List(p * 2))

        val result = for {
          result1 <- computeHeavy("abc")
          result2 <- computeHeavy(result1)
          x = result2.isEmpty
        } yield x

        def computeHeavy(value: String) : Future[String] = Future{s"$value + $value"}

        ???
      }
    }

  }

  private def clone(goat:Goat, index:Int) =  goat.copy(name = goat.name + s"_$index", id = Some(UUID.randomUUID().toString))

}
