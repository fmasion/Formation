package controllers

import java.util.UUID

import models.Goat
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.Future


class g2 extends Controller {

  lazy val f1 =  Future.successful(1)
  val f2 =  Future.successful(2)
  val f3 =  Future.successful(3)
//  val f1 =  List(1,2) //Future.successful(1)
//  val f2 =  List("3","4") //Future.successful(2)

  // temps  => max(future)
  val toto: Future[Int] = for {
    v1 <- f1
    v2 <- f2
    v3 <- f3
  } yield{
    v1 + v2 + v3
  }

  f1.flatMap(v1 => f2.flatMap(v2 => f3.map(v3 => v1 + v2 + v3)))

  // temps => SUM(future)
  val toto2: Future[Int] = for {
    v1 <- Future.successful(1)
    v2 <- Future.successful(2)
    v3 <- Future.successful(3)
  } yield{
    v1 + v2 + v3
  }


  // temps  => PLUS PLUS RAPIDE POSSIBLE
  val toto3 = for {
    v1 <- f1
    v2 <- f2
    v3 <- toto(v2)
  } yield{
    v1 + v2 + v3
  }

  val toto4 = for {
    v1 <- f1
    v2 <- f2
    v3 <- for{
      v1 <- Future.successful(v2)
      v2 <- f2
    }yield{
      v1 * v2
    }
  } yield{
    v1 + v2 + v3
  }

  def toto(v2 : Int) = for{
    v1 <- Future.successful(v2)
    v2 <- f2
  }yield{
    v1 * v2
  }

}
