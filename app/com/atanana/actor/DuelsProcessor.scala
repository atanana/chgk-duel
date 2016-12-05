package com.atanana.actor

import javax.inject.Inject

import akka.actor.Actor
import com.atanana.parsers.TeamParser
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class DuelsProcessor @Inject()(ws: WSClient, teamParser: TeamParser) extends Actor {
  val TEAM_ADDRESS = "http://rating.chgk.info/team/"

  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      teamPage(duelRequest.teamId1)
        .zip(teamPage(duelRequest.teamId2))
        .onComplete {
          case Success((teamPage1, teamPage2)) =>
            val tournaments = teamParser.parse(teamPage1).toSet.intersect(teamParser.parse(teamPage2).toSet)
            duelRequest.listener ! DuelResult(tournaments.toString(), duelRequest.uuid)
          case Failure(exception) => //todo show error
        }
  }

  private def teamPage(teamId: Long) = {
    request(TEAM_ADDRESS + teamId)
  }

  private def tournamentInfo(tournamentId: Long, teamId1: Long, teamId2: Long) = {
    val infoLink = tournamentInfoLink(tournamentId)
    request(infoLink(teamId1))
      .zip(request(infoLink(teamId1)))
  }

  private def tournamentInfoLink(tournamentId: Long)(teamId: Long) = {
    s"http://rating.chgk.info/api/tournaments/$tournamentId/results/$teamId.json"
  }

  private def request(url: String) = {
    ws.url(url).get().map(_.body)
  }
}