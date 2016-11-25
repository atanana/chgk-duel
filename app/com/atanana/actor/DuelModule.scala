package com.atanana.actor

import javax.inject.{Named, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.{AbstractModule, Provides}


class DuelModule extends AbstractModule {
  override def configure(): Unit = {
  }

  @Provides
  @Singleton
  @Named("DuelsProcessor")
  def duelsProcessor(actorSystem: ActorSystem): ActorRef = {
    actorSystem.actorOf(DuelsProcessor.props)
  }
}
