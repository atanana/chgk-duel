package com.atanana.actor

import java.util.UUID

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.testkit.TestProbe
import akka.util.Timeout
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class DuelsQueueTest extends ActorSpec with BeforeAndAfter {
  private val listenerProbe: TestProbe = TestProbe()
  private val routerProbe: TestProbe = TestProbe()
  private var actor: ActorRef = _
  private val uuid: UUID = UUID.randomUUID()

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

    "provides queue state" in {
      actor ! DuelRequest(listenerProbe.ref, uuid)
      implicit val timeout = Timeout(5 seconds)
      val future = (actor ? DuelsQueueStateRequest()).mapTo[DuelsQueueState]
      val queueState = Await.result(future, 5 seconds)
      queueState.requests shouldEqual List(uuid.toString)
    }
  }
}
