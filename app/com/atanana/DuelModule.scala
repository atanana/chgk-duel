package com.atanana

import javax.inject.{Named, Singleton}

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.atanana.actor.{DuelsProcessor, DuelsQueue}
import com.atanana.parsers.{SiteJsonParser, TeamParser}
import com.google.inject.{AbstractModule, Provides}
import play.api.libs.concurrent.AkkaGuiceSupport


class DuelModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[DuelsQueue]("DuelsQueue")

    bind(classOf[TeamParser])
    bind(classOf[SiteConnector])
    bind(classOf[SiteJsonParser])
    bind(classOf[DuelResultGenerator])
  }

  @Provides
  @Singleton
  @Named("DuelsProcessorRouter")
  def duelsProcessorRouter(actorSystem: ActorSystem, duelResultGenerator: DuelResultGenerator): ActorRef = {
    val props = Props(new DuelsProcessor(duelResultGenerator))
    actorSystem.actorOf(RoundRobinPool(1).props(props), "DuelsProcessorRouter")
  }
}
