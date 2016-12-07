package com.atanana.actor

import javax.inject.Inject

import akka.actor.{Actor, Props}
import com.atanana.DuelResultGenerator

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
      val resultFuture = duelResultGenerator.generate(duelRequest.teamId1, duelRequest.teamId2)
      resultFuture.onComplete {
        case Success(teams) =>
          duelRequest.listener ! DuelResult(teams.toString(), duelRequest.uuid)
        case Failure(exception) =>
      }
      Await.ready(resultFuture, 5 minutes)
  }
}