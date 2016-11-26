package controllers

import javax.inject.{Inject, Named, Singleton}

import com.atanana.actor.SocketHandler
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Controller, WebSocket}

@Singleton
class DuelController @Inject()(implicit actorSystem: ActorSystem,
                               materializer: Materializer,
                               @Named("DuelsQueue") processor: ActorRef) extends Controller {
  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => SocketHandler.props(out, processor))
  }
}
