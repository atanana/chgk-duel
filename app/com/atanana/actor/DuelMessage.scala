package com.atanana.actor

import java.util.UUID

import akka.actor.ActorRef
import com.atanana.TeamDuelResult
import play.api.libs.json.{JsObject, Json, Writes}

sealed abstract class DuelMessage(val strType: String) {
  def toJson: JsObject = {
    Json.obj(
      "type" -> strType
    )
  }
}

case class DuelRequest(listener: ActorRef, uuid: UUID, team1Id: Long, team2Id: Long) extends DuelMessage("DuelRequest")

case class DuelResult(team1Result: TeamDuelResult, team2Result: TeamDuelResult, uuid: UUID) extends DuelMessage("DuelResult") {
  implicit val teamResultWrites: Writes[TeamDuelResult] = new Writes[TeamDuelResult] {
    override def writes(o: TeamDuelResult): JsObject = Json.obj(
      "name" -> o.name,
      "town" -> o.town,
      "wins" -> o.wins,
      "total" -> o.totalQuestions
    )
  }

  override def toJson: JsObject = super.toJson ++ Json.obj(
    "team1" -> team1Result,
    "team2" -> team2Result
  )
}

case class DuelFailure(team1Id: Long, team2Id: Long, uuid: UUID) extends DuelMessage("DuelFailure")

case class DuelsQueueState(requests: List[String]) extends DuelMessage("DuelsQueueState") {
  override def toJson: JsObject = super.toJson ++ Json.obj(
    "requests" -> requests
  )
}

case class DuelRequestAccepted(uuid: UUID) extends DuelMessage("DuelRequestAccepted") {
  override def toJson: JsObject = super.toJson ++ Json.obj(
    "uuid" -> uuid
  )
}

case class ClientDuelRequest(teamId1: Long, teamId2: Long)

case class DuelsQueueStateRequest()