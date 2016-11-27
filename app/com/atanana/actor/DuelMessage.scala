package com.atanana.actor

import java.util.UUID

import akka.actor.ActorRef
import play.api.libs.json.{JsObject, JsValue, Json}

sealed abstract class DuelMessage(val strType: String) {
  def toJson: JsObject = {
    Json.obj(
      "type" -> strType
    )
  }
}

case class DuelRequest(listener: ActorRef, uuid: UUID) extends DuelMessage("DuelRequest")

case class DuelResult(message: String, uuid: UUID) extends DuelMessage("DuelResult") {
  override def toJson: JsObject = super.toJson ++ Json.obj(
    "message" -> message
  )
}

case class DuelsQueueState(requests: List[String]) extends DuelMessage("DuelsQueueState") {
  override def toJson: JsObject = super.toJson ++ Json.obj(
    "requests" -> requests
  )
}