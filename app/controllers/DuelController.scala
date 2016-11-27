package controllers

import javax.inject.{Inject, Named, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import com.atanana.actor.{DuelMessage, SocketHandler}
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc.{Controller, WebSocket}

@Singleton
class DuelController @Inject()(implicit actorSystem: ActorSystem,
                               materializer: Materializer,
                               @Named("DuelsQueue") processor: ActorRef) extends Controller {
  implicit val messageFlowTransformer: MessageFlowTransformer[String, DuelMessage] = MessageFlowTransformer.jsonMessageFlowTransformer[String, DuelMessage](
    (JsPath \ "value").read[String],
    Writes.apply(_.toJson)
  )

  def socket: WebSocket = WebSocket.accept[String, DuelMessage] { request =>
    ActorFlow.actorRef(out => SocketHandler.props(out, processor))
  }
}
