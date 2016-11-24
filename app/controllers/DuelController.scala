package controllers

import javax.inject.{Inject, Singleton}

import actor.MyWebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Controller, WebSocket}

@Singleton
class DuelController @Inject()(implicit actorSystem: ActorSystem, materializer: Materializer) extends Controller {
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => MyWebSocketActor.props(out))
  }
}
