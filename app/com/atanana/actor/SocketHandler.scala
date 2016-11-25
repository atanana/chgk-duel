package com.atanana.actor

import akka.actor._

object SocketHandler {
  def props(out: ActorRef, processor: ActorRef) = Props(new SocketHandler(out, processor))
}

class SocketHandler(out: ActorRef, processor: ActorRef) extends Actor {
  def receive: Receive = {
    case msg: String =>
      processor ! DuelRequest(out)
  }
}