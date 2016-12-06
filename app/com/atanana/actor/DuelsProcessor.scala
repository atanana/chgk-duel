package com.atanana.actor

import javax.inject.Inject

import akka.actor.Actor
import com.atanana.SiteConnector
import com.atanana.parsers.TeamParser
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class DuelsProcessor @Inject()(ws: WSClient, teamParser: TeamParser, siteConnector: SiteConnector) extends Actor {
  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      siteConnector.teamPage(duelRequest.teamId1)
        .zip(siteConnector.teamPage(duelRequest.teamId2))
        .onComplete {
          case Success((teamPage1, teamPage2)) =>
            val tournaments = teamParser.parse(teamPage1).toSet.intersect(teamParser.parse(teamPage2).toSet)
            duelRequest.listener ! DuelResult(tournaments.toString(), duelRequest.uuid)
          case Failure(exception) => //todo show error
        }
  }
}