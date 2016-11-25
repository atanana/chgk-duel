package com.atanana.actor

import akka.actor.{Actor, ActorRef, Props}

object DuelsProcessor {
  val props = Props(new DuelsProcessor)
}

class DuelsProcessor extends Actor {
  private var duelCounter: Int = 0

  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      Thread.sleep(2000)
      duelCounter += 1
      duelRequest.listener ! s"Duel $duelCounter processed"
  }
}

case class DuelRequest(listener: ActorRef)