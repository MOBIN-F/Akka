package com.mobin

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by Mobin on 2017/7/23.
  */
object SupervisMain {
  def main(args: Array[String]) {
     val system = ActorSystem()
    val supervisingActor = system.actorOf(Props[SupervisingActor], "supervising-actor")
    supervisingActor ! "failChild"
  }
}

class SupervisingActor extends Actor{
  val child = context.actorOf(Props[SupervisedActor], "supervised-actor")
  override def receive: Receive = {
    case "failChild" => child!"fail"
  }
}
class SupervisedActor extends Actor{
  override def preStart(): Unit = println("supervised start")
  override def postStop(): Unit = println("supervised stop")
  override def receive: Actor.Receive = {
    case "fail" =>
      println("supervised actor fails now")
      throw new Exception("I failed !")
  }
}
