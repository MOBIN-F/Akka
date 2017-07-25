package com.mobin.conf

import akka.actor.{ActorRef, ActorLogging, Actor}

import scala.concurrent.duration.FiniteDuration

/**
  * Created by Mobin on 2017/7/24.
  */
class HelloWorld extends Actor
     with ActorLogging{
  override def receive = {
    case msg: String =>
      val hello = "Hello %s".format(msg)
      sender() ! hello
      log.info("Sent response{}", hello)
  }
}

class HelloWorldCaller(timer: FiniteDuration, actor: ActorRef)
     extends Actor with ActorLogging {
  case class TimerTick(msg: String)

  override def preStart(): Unit = {
    super.preStart()
    implicit val ec = context.dispatcher
    context.system.scheduler.schedule(
      timer,
      timer,
      self,
      new TimerTick("everybody")
    )
  }
  override def receive = {
    case msg: String => log.info("received {}",msg)
    case tick: TimerTick => actor! tick.msg
  }
}
