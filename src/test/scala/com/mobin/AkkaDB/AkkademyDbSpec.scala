package com.mobin.AkkaDB

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.{Matchers, FunSpecLike}
import akka.util.Timeout

/**
  * Created by Mobin on 2017/8/6.
  */
class AkkademyDbSpec extends FunSpecLike with Matchers{
  implicit val system = ActorSystem()

  describe("akkademyDb") {
    describe("give SetRequest") {
      it ("should place key/value into map"){
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! SetRequest("key" ,"value")

        val akkaadmeyDb = actorRef.underlyingActor
        akkaadmeyDb.map.get("key") should equal(Some("value"))
      }
    }
  }
}
