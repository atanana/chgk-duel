package com.atanana.actor

import javax.inject.Inject

import akka.actor.Actor
import play.api.libs.ws.WSClient

import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

class DuelsProcessor @Inject()(ws: WSClient) extends Actor {
  val TEAM_ADDRESS = "http://rating.chgk.info/team/"

  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      teamPage(duelRequest.teamId1)
        .zip(teamPage(duelRequest.teamId2))
        .onComplete {
          case Success((teamPage1, teamPage2)) =>
          case Failure(exception) => //todo show error
        }
  }

  private def teamPage(teamId: Long) = {
    ws.url(TEAM_ADDRESS + teamId).get().map(_.body)
  }
}