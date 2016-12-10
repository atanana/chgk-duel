package com.atanana.actor

import javax.inject.Inject

import akka.actor.{Actor, Props}
import com.atanana.DuelResultGenerator
import play.api.Logger

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object DuelsProcessor {
  def props(duelResultGenerator: DuelResultGenerator) = Props(new DuelsProcessor(duelResultGenerator))
}

class DuelsProcessor @Inject()(duelResultGenerator: DuelResultGenerator) extends Actor {
  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      try {
        Logger.info("Start processing: " + duelRequest)
        val resultFuture = duelResultGenerator.generate(duelRequest.team1Id, duelRequest.team2Id)
        resultFuture.onComplete {
          case Success((team1, team2)) =>
            Logger.info("Send to client: " + (team1, team2))
            duelRequest.listener ! DuelResult(team1, team2, duelRequest.uuid)
          case Failure(exception) =>
            handleException(duelRequest, exception)
        }
        Await.ready(resultFuture, 5 minutes)
      } catch {
        case exception: Throwable =>
          handleException(duelRequest, exception)
      }
  }

  private def handleException(duelRequest: DuelRequest, exception: Throwable) = {
    Logger.error("Error on processing duel request!", exception)
    duelRequest.listener ! DuelFailure(duelRequest.team1Id, duelRequest.team2Id, duelRequest.uuid)
  }
}