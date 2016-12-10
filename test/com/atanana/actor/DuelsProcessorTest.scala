package com.atanana.actor

import java.util.UUID

import akka.actor.ActorRef
import akka.testkit.TestProbe
import com.atanana.{DuelResultGenerator, TeamDuelResult}
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfter
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DuelsProcessorTest extends ActorSpec with BeforeAndAfter with MockitoSugar {
  val team1Id = 123
  val team2Id = 321

  var listenerProbe: TestProbe = _
  var duelResultGenerator: DuelResultGenerator = _
  var processor: ActorRef = _

  before {
    listenerProbe = TestProbe()
    duelResultGenerator = mock[DuelResultGenerator]
    processor = system.actorOf(DuelsProcessor.props(duelResultGenerator))
  }

  "Actor" must {
    "process duel request" in {
      val result1 = TeamDuelResult("test 1", "town 1", 2, 31)
      val result2 = TeamDuelResult("test 2", "town 2", 4, 34)
      when(duelResultGenerator.generate(team1Id, team2Id)).thenReturn(Future((
        result1,
        result2
      )))
      val uuid = UUID.randomUUID()

      processor ! DuelRequest(listenerProbe.ref, uuid, team1Id, team2Id)

      listenerProbe.expectMsg(DuelResult(result1, result2, uuid))
    }

    "notify listener about error #1" in {
      when(duelResultGenerator.generate(team1Id, team2Id)).thenReturn(Future {
        throw new RuntimeException
      })
      val uuid = UUID.randomUUID()

      processor ! DuelRequest(listenerProbe.ref, uuid, team1Id, team2Id)

      listenerProbe.expectMsg(DuelFailure(team1Id, team2Id, uuid))
    }

    "notify listener about error #2" in {
      when(duelResultGenerator.generate(team1Id, team2Id)).thenThrow(new RuntimeException)
      val uuid = UUID.randomUUID()

      processor ! DuelRequest(listenerProbe.ref, uuid, team1Id, team2Id)

      listenerProbe.expectMsg(DuelFailure(team1Id, team2Id, uuid))
    }
  }
}
