package com.atanana.actor

import akka.actor.ActorRef
import akka.testkit.TestProbe
import org.scalatest.BeforeAndAfter

class SocketHandlerTest extends ActorSpec with BeforeAndAfter {
  private var outProbe: TestProbe = _
  private var processorProbe: TestProbe = _
  private var actor: ActorRef = _

  before {
    outProbe = TestProbe()
    processorProbe = TestProbe()
    actor = system.actorOf(SocketHandler.props(outProbe.ref, processorProbe.ref))
  }

  "Actor" must {
    "generate duel request" in {
      actor ! ClientDuelRequest(1, 2)

      outProbe.expectMsgPF() {
        case DuelRequestAccepted(_) =>
      }

      processorProbe.expectMsgPF() {
        case DuelRequest(actorRef, _) =>
          actorRef shouldEqual outProbe.ref
        case DuelsQueueStateRequest() =>
      }
    }

    "transmit queue state" in {
      val message = DuelsQueueState(List("test 1", "test 2", "test 3"))
      actor ! message
      outProbe.expectMsg(message)
    }

    "provide queue state on start" in {
      processorProbe.expectMsg(DuelsQueueStateRequest())
      val queueState = DuelsQueueState(List("1", "2", "3"))
      processorProbe.reply(queueState)
      outProbe.expectMsg(queueState)
    }
  }
}
