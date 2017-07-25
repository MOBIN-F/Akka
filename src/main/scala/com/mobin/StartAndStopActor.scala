package com.mobin

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by Mobin on 2017/7/23.
  * preStart() :在处理第一条消息之前将被调用
    *postStop() i：在Actor！"stop"停止之前被调用
  */
object StartAndStopActor {
  def main(args: Array[String]) {
    val system = ActorSystem()
    val first = system.actorOf(Props[StartstopActor1], "first")
    //first ! "stop"
    first.tell("stop",null)
  }
}

class StartstopActor1 extends Actor{
  override def preStart(): Unit = {
    println("first started")
    context.actorOf(Props[StartstopActor2], "second")
  }
  override def postStop(): Unit = println("first stopped")
  override def receive: Receive = {
    case "stop" => println("55");context.stop(self);println("88")
  }
}

class StartstopActor2 extends Actor{
  override def preStart(): Unit = println("second started")
  override def postStop(): Unit = println("second stopped")
  override def receive: Actor.Receive = Actor.emptyBehavior
}

