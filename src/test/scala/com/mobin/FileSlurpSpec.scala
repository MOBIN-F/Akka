package com.mobin

import java.nio.file.Paths

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.mobin.AkkaIO.File.ReadResult
import com.mobin.AkkaIO.FileSlurp
import com.mobin.AkkaIO.FileSlurp.Done
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.io.Source

class FileSlurpSpec extends TestKit(ActorSystem("system")) with WordSpecLike with Matchers with ImplicitSender with BeforeAndAfterAll {

  override def afterAll() {
    new java.io.File("/tmp/test-file.txt").delete()
    system.shutdown()
  }

  "A FileSlurp" should {
    "read a whole file in correct order" in {
      com.io.printToFile(new java.io.File("/tmp/test-file.txt")) { p =>
        for (i <- 0 until 1000000) p.print(util.Random.nextPrintableChar())
      }

      implicit val system = ActorSystem("system")

      val slurp = system.actorOf(Props(classOf[FileSlurp], Paths.get("/tmp", "test-file.txt"), self))

      watch(slurp)

      val chunks = receiveWhile() {
        case ReadResult(bytes, _, _) => bytes.utf8String
      }

      val content = chunks.mkString

      content.size should be(1000000)
      content should be(Source.fromFile("/tmp/test-file.txt").mkString)

      expectMsg(Done)

      expectTerminated(slurp)
    }
  }
}
