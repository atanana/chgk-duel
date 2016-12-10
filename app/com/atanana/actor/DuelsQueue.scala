package com.atanana.actor

import java.util.UUID
import javax.inject.{Inject, Named}

import akka.actor.{Actor, ActorRef}

import scala.collection.immutable

class DuelsQueue @Inject()(@Named("DuelsProcessorRouter") private val router: ActorRef) extends Actor {
  var queue: List[DuelRequest] = immutable.List.empty

  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      queue = queue :+ duelRequest
      router ! duelRequest.copy(listener = self)
      queueUpdated()
    case duelResult: DuelResult =>
      processDuelResult(duelResult, duelResult.uuid)
    case duelFailure: DuelFailure =>
      processDuelResult(duelFailure, duelFailure.uuid)
    case _: DuelsQueueStateRequest =>
      sender() ! queueState
  }

  private def processDuelResult(message: AnyRef, uuid: UUID) = {
    queue.find(_.uuid == uuid)
      .foreach(duelRequest => {
        duelRequest.listener ! message
        queue = queue diff List(duelRequest)
        queueUpdated()
      })
  }

  private def queueUpdated() = {
    context.system.actorSelection("/user/*") ! queueState
  }

  private def queueState = {
    val requests = queue.map(_.uuid.toString)
    DuelsQueueState(requests)
  }
}