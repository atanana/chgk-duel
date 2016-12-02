package com.atanana

import javax.inject.{Named, Singleton}

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.atanana.actor.{DuelsProcessor, DuelsQueue}
import com.google.inject.{AbstractModule, Provides}
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.libs.ws.WSClient


class DuelModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[DuelsQueue]("DuelsQueue")
  }

  @Provides
  @Singleton
  @Named("DuelsProcessorRouter")
  def duelsProcessorRouter(actorSystem: ActorSystem, ws: WSClient): ActorRef = {
    actorSystem.actorOf(RoundRobinPool(1).props(Props(new DuelsProcessor(ws))), "DuelsProcessorRouter")
  }
}
