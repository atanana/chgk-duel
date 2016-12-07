package com.atanana.actor

import java.util.UUID

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.testkit.TestProbe
import akka.util.Timeout
import com.atanana.TeamDuelResult
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class DuelsQueueTest extends ActorSpec with BeforeAndAfter {
  private val listenerProbe: TestProbe = TestProbe()
  private val routerProbe: TestProbe = TestProbe()
  private val uuid: UUID = UUID.randomUUID()
  private var actor: ActorRef = _

  before {
    actor = system.actorOf(Props(new DuelsQueue(routerProbe.ref)))
  }

  "Actor" must {
    "refers to processor duel request" in {
      actor ! DuelRequest(listenerProbe.ref, uuid, 1, 2)
      routerProbe.expectMsg(DuelRequest(actor, uuid, 1, 2))
    }

    "notifies listener when request processed" in {
      actor ! DuelRequest(listenerProbe.ref, uuid, 1, 2)
      val result = DuelResult(mock[TeamDuelResult], mock[TeamDuelResult], uuid)
      actor ! result
      listenerProbe.expectMsg(result)
    }

    "provides queue state" in {
      actor ! DuelRequest(listenerProbe.ref, uuid, 1, 2)
      implicit val timeout = Timeout(5 seconds)
      val future = (actor ? DuelsQueueStateRequest()).mapTo[DuelsQueueState]
      val queueState = Await.result(future, 5 seconds)
      queueState.requests shouldEqual List(uuid.toString)
    }
  }
}
