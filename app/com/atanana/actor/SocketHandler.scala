package com.atanana.actor

import java.util.UUID

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import scala.language.postfixOps

import scala.concurrent.ExecutionContext.Implicits.global

object SocketHandler {
  def props(out: ActorRef, processor: ActorRef) = Props(new SocketHandler(out, processor))
}

class SocketHandler(out: ActorRef, queue: ActorRef) extends Actor {
  implicit val timeout = Timeout(5 seconds)

  def receive: Receive = {
    case duelRequest: ClientDuelRequest =>
      val uuid = UUID.randomUUID()
      out ! DuelRequestAccepted(uuid)
      queue ! DuelRequest(out, uuid, duelRequest.teamId1, duelRequest.teamId2)
    case queueState: DuelsQueueState =>
      out ! queueState
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()

    (queue ? DuelsQueueStateRequest())
      .onSuccess {
        case queueState: DuelsQueueState => out ! queueState
      }
  }

  //todo cancel duel on close websocket
}