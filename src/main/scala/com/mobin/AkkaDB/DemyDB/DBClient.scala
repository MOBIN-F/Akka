package com.mobin.AkkaDB.DemyDB

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout

/**
  * Created by Mobin on 2017/8/10.
  */
class DBClient(remoteAddress: String) {
  private implicit val timeout = Timeout(2, TimeUnit.SECONDS)
  private implicit val system = ActorSystem("LocalSystem")
  private val remoteDb = system.actorSelection(
    s"akka.tcp://akkademy@remoteAddress/user/akkademy-db"
  )
  def set(key : String, value: Object) = {
    remoteDb ? SetRequest(key, value)
  }
  def get(key: String) = {
    remoteDb ? GetRequest(key)
  }
}
