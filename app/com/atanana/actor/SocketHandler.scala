package com.atanana.actor

import java.util.UUID

import akka.actor._

object SocketHandler {
  def props(out: ActorRef, processor: ActorRef) = Props(new SocketHandler(out, processor))
}

class SocketHandler(out: ActorRef, processor: ActorRef) extends Actor {
  def receive: Receive = {
    case _: String =>
      processor ! DuelRequest(out, UUID.randomUUID())
    case queueState: DuelsQueueState =>
      out ! queueState
  }

  //todo cancel duel on close websocket
}