package com.atanana.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class SocketHandlerTest extends TestKit(ActorSystem("test")) with WordSpecLike with BeforeAndAfterAll with Matchers {
  private val outProbe = TestProbe()
  private val processorProbe = TestProbe()
  private val actor: ActorRef = TestActorRef(SocketHandler.props(outProbe.ref, processorProbe.ref))

  "Actor" must {
    "generate duel request" in {
      actor ! "test"
      processorProbe.expectMsgPF() {
        case DuelRequest(actorRef, _) =>
          actorRef shouldEqual outProbe.ref
      }
    }

    "transmit queue state" in {
      val queueState = List("test 1", "test 2", "test 3")
      actor ! DuelsQueueState(queueState)
      outProbe.expectMsgPF() {
        case message: String =>
          message shouldEqual queueState.toString()
      }
    }
  }

  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)
}
