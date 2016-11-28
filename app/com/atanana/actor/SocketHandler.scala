package com.atanana.actor

import java.util.UUID

import akka.actor._

object SocketHandler {
  def props(out: ActorRef, processor: ActorRef) = Props(new SocketHandler(out, processor))
}

class SocketHandler(out: ActorRef, processor: ActorRef) extends Actor {
  def receive: Receive = {
    case _: ClientDuelRequest =>
      val uuid = UUID.randomUUID()
      out ! DuelRequestAccepted(uuid)
      processor ! DuelRequest(out, uuid)
    case queueState: DuelsQueueState =>
      out ! queueState
  }

  //todo cancel duel on close websocket
}