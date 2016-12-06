package com.atanana

import javax.inject.Inject

import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SiteConnector {
  val SITE = "http://rating.chgk.info"
}

class SiteConnector @Inject()(ws: WSClient) {

  import SiteConnector.SITE

  def teamPage(teamId: Long): Future[String] = {
    request(s"$SITE/team/" + teamId)
  }

  def teamInfo(teamId: Long): Future[String] = {
    request(s"$SITE/api/teams/$teamId.json")
  }

  def tournamentInfo(tournamentId: Long, teamId1: Long, teamId2: Long): Future[(String, String)] = {
    val infoLink = tournamentInfoLink(tournamentId)(_)
    request(infoLink(teamId1))
      .zip(request(infoLink(teamId2)))
  }

  private def tournamentInfoLink(tournamentId: Long)(teamId: Long) = {
    s"$SITE/api/tournaments/$tournamentId/results/$teamId.json"
  }

  private def request(url: String) = {
    ws.url(url).get().map(_.body)
  }
}
