package com.mobin.conf

import akka.actor.{Props, ActorSystem}
import scala.concurrent.duration._
/**
  * Created by Mobin on 2017/7/24.
  */
object BootHello extends App{
  val system = ActorSystem()
  val actor = system.actorOf(Props[HelloWorld])
  val config = system.settings.config
  val timer = config.getInt("akka.timer")
  system.actorOf(Props(new HelloWorldCaller(timer millis,actor)))
}
