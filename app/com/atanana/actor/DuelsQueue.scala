package com.atanana.actor

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinPool

import scala.collection.immutable

class DuelsQueue extends Actor {
  val router: ActorRef = context.actorOf(RoundRobinPool(1).props(Props(classOf[DuelsProcessor])), "DuelsProcessorRouter")
  var queue: List[DuelRequest] = immutable.List.empty

  override def receive: Receive = {
    case duelRequest: DuelRequest =>
      queue = queue :+ duelRequest
      router ! duelRequest.copy(listener = self)
    case duelResult: DuelResult =>
      queue.find(_.uuid == duelResult.uuid)
        .foreach(duelRequest => {
          duelRequest.listener ! duelResult.message
          queue = queue diff List(duelRequest)
        })
  }
}
