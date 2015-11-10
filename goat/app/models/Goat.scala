package models

import play.api.libs.json.Json

/**
 * Created by fred on 21/10/2015.
 */
case class Goat(name:String, color:String, id:Option[String]=None)

object Goat{
  //implicit val goatFormat= Json.format[Goat]
  implicit val goatReads= Json.reads[Goat]
  implicit val goatWrites= Json.writes[Goat]
}
