package com.atanana

import javax.inject.{Named, Singleton}

import akka.actor.{ActorRef, ActorSystem, Props}
import com.atanana.actor.DuelsQueue
import com.google.inject.{AbstractModule, Provides}


class DuelModule extends AbstractModule {
  override def configure(): Unit = {
  }

  @Provides
  @Singleton
  @Named("DuelsQueue")
  def duelsProcessor(actorSystem: ActorSystem): ActorRef = {
    actorSystem.actorOf(Props(classOf[DuelsQueue]))
  }
}
