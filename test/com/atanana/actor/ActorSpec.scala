package com.atanana.actor

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

abstract class ActorSpec extends TestKit(ActorSystem("test")) with WordSpecLike with BeforeAndAfterAll with Matchers {
  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)
}
