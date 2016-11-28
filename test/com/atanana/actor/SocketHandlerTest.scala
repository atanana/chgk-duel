package com.atanana.actor

import akka.actor.ActorRef
import akka.testkit.TestProbe

class SocketHandlerTest extends ActorSpec {
  private val outProbe = TestProbe()
  private val processorProbe = TestProbe()
  private val actor: ActorRef = system.actorOf(SocketHandler.props(outProbe.ref, processorProbe.ref))

  "Actor" must {
    "generate duel request" in {
      actor ! ClientDuelRequest(1, 2)

      outProbe.expectMsgPF() {
        case DuelRequestAccepted(_) =>
      }

      processorProbe.expectMsgPF() {
        case DuelRequest(actorRef, _) =>
          actorRef shouldEqual outProbe.ref
      }
    }

    "transmit queue state" in {
      val message = DuelsQueueState(List("test 1", "test 2", "test 3"))
      actor ! message
      outProbe.expectMsg(message)
    }
  }
}
