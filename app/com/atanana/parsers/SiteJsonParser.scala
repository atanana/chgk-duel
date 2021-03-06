package com.atanana.parsers

import play.api.libs.json._
import play.api.libs.functional.syntax._

object SiteJsonParser {
  val teamInfoReads: Reads[TeamInfo] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "town").read[String]
    ) (TeamInfo.apply _)
}

class SiteJsonParser {

  import SiteJsonParser.teamInfoReads

  def parseTeamInfo(teamInfo: String): TeamInfo = {
    val teamJson = Json.parse(teamInfo).as[JsArray].head.as[JsObject]
    teamInfoReads.reads(teamJson).get
  }

  def parseTeamResult(teamResult: String): Int = {
    (Json.parse(teamResult) \\ "mask")
      .flatMap(_.as[JsArray].value)
      .count(_ == JsString("1"))
  }
}

case class TeamInfo(name: String, town: String)