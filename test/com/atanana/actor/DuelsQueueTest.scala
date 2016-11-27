package com.atanana.actor

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest._

class DuelsQueueTest extends ActorSpec with BeforeAndAfter {
  private val listenerProbe: TestProbe = TestProbe()
  private val routerProbe: TestProbe = TestProbe()
  private var actor: ActorRef = _
  private val uuid = UUID.randomUUID()

  before {
    actor = system.actorOf(Props(new DuelsQueue(routerProbe.ref)))
  }

  "Actor" must {
    "refers to processor duel request" in {
      actor ! DuelRequest(listenerProbe.ref, uuid)
      routerProbe.expectMsg(DuelRequest(actor, uuid))
    }

    "notifies listener when request processed" in {
      actor ! DuelRequest(listenerProbe.ref, uuid)
      val result = DuelResult("test message", uuid)
      actor ! result
      listenerProbe.expectMsg(result)
    }
  }
}
