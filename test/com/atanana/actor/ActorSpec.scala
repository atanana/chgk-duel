package com.atanana.actor

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

abstract class ActorSpec extends TestKit(ActorSystem("test")) with WordSpecLike with BeforeAndAfterAll with Matchers
  with MockitoSugar {
  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)
}
