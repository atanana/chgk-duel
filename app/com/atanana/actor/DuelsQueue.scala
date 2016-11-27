package com.atanana.actor

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
      queue.find(_.uuid == duelResult.uuid)
        .foreach(duelRequest => {
          duelRequest.listener ! duelResult
          queue = queue diff List(duelRequest)
          queueUpdated()
        })
  }

  private def queueUpdated() = {
    val requests = queue.map(_.uuid.toString)
    context.system.actorSelection("/user/*") ! DuelsQueueState(requests)
  }
}