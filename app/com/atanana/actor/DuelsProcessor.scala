package com.atanana.actor

import javax.inject.Inject

import akka.actor.Actor
import com.atanana.parsers.TeamParser
import play.api.libs.ws.WSClient

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

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
    ws.url(TEAM_ADDRESS + teamId).get().map(_.body)
  }
}