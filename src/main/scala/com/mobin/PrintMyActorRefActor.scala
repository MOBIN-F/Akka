package com.mobin

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by Mobin on 2017/7/23.
  */
class PrintMyActorRefActor extends Actor{
  override def receive: Receive = {
    case "printit" =>
      val secondRef = context.actorOf(Props.empty, "second-actor")
      println(s"Second: $secondRef")

  }
}

object PrintMyActorRefActor{
  def main(args: Array[String]) {
    val system = ActorSystem()
    val firstRef = system.actorOf(Props[PrintMyActorRefActor], "first-actor")
    println(s"first: $firstRef")
    firstRef! "printit"
  }
}
