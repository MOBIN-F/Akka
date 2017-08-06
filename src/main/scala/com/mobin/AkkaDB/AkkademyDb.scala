package com.mobin.AkkaDB

import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable.HashMap

/**
  * Created by Mobin on 2017/8/6.
  */
case class SetRequest(key: String, value: Object)

class AkkademyDb extends Actor{
  val map = new HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) => {
      log.info("received SetRequest - key: {} value : {}", key,value)
      map.put(key ,value)
    }
    case o => log.info("received unknow message: {}", o)
  }
}
