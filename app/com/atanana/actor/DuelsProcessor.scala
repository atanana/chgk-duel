package com.atanana.actor

import java.util.UUID

import akka.actor.{Actor, ActorRef}

class DuelsProcessor extends Actor {
  private var duelCounter: Int = 0

  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      Thread.sleep(2000)
      duelCounter += 1
      duelRequest.listener ! DuelResult(s"Duel $duelCounter processed", duelRequest.uuid)
  }
}

case class DuelRequest(listener: ActorRef, uuid: UUID)

case class DuelResult(message: String, uuid: UUID)