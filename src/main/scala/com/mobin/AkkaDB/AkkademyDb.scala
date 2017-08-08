package com.mobin.AkkaDB

import akka.actor.{Props, ActorSystem, Status, Actor}
import akka.event.Logging

import scala.collection.mutable.HashMap

/**
  * Created by Mobin on 2017/8/6.
  */
case class SetRequest(key: String, value: Object)
case class GetRequest(key: String)
case class KeyNotFoundException(key: String) extends Exception   //定义异常类

class AkkademyDb extends Actor{
  val map = new HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) => {
      log.info("received SetRequest - key: {} value : {}", key,value)
      map.put(key ,value)
      sender() ! Status.Success
    }
    case GetRequest(key) => {
      log.info("received GetRequest - key: {}", key)
      val response = map.get(key)
      response match {
        case Some(x) => sender() ! x     //找到key就返回
        case None => sender()! Status.Failure(new KeyNotFoundException(key))   //否则抛出异常
      }
    }
    case o => Status.Failure(new ClassNotFoundException)
  }

}
object Main extends App{
  val system = ActorSystem("akkademy")
  system.actorOf(Props[AkkademyDb], "akkademy-db")
}
