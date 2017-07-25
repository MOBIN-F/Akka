package com.mobin

import java.nio.file.Paths

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.ByteString
import com.mobin.AkkaIO.File.{Close, Closed, Written}
import com.mobin.AkkaIO.FileWriter
import com.mobin.AkkaIO.FileWriter.{Write, WriteLine}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.io.Source

class FileWriterSpec extends TestKit(ActorSystem("system")) with WordSpecLike with Matchers with ImplicitSender with BeforeAndAfterAll {
  override def afterAll() = system.terminate()

  "A FileWriter" should {
    "write to a file in correct order" in {
      try {
        val writer = system.actorOf(Props(classOf[FileWriter], Paths.get("/tmp", "test-file.txt")))

        watch(writer)

        writer ! Write(ByteString("test"))
        writer ! Write(ByteString("foo"))
        writer ! Write(ByteString("bar"))

        expectMsgAllOf(Written(4), Written(3), Written(3))

        writer ! Close
        expectMsg(Closed)
        expectTerminated(writer)

        Source.fromFile("/tmp/test-file.txt").mkString should be("testfoobar")
      } finally new java.io.File("/tmp/test-file.txt").delete()
    }

    "correctly add newlines on WriteLine" in {
      try {
        val writer = system.actorOf(Props(classOf[FileWriter], Paths.get("/tmp", "test-file.txt")))

        watch(writer)

        writer ! WriteLine(ByteString("test"))
        writer ! WriteLine(ByteString("foo"))
        writer ! Write(ByteString("bar"))

        receiveN(3)

        writer ! Close
        expectMsg(Closed)
        expectTerminated(writer)

        Source.fromFile("/tmp/test-file.txt").mkString should be(Seq("test", "foo", "bar").mkString(System.lineSeparator()))
      } finally new java.io.File("/tmp/test-file.txt").delete()
    }

    "be able append to a file" in {
      try {
        com.io.printToFile(new java.io.File("/tmp/test-file.txt")) { p =>
          p.print("foobar")
        }

        val writer = system.actorOf(Props(classOf[FileWriter], Paths.get("/tmp", "test-file.txt"), true))

        watch(writer)

        writer ! Write(ByteString("baz"))

        expectMsg(Written(3))

        writer ! Close
        expectMsg(Closed)
        expectTerminated(writer)

        Source.fromFile("/tmp/test-file.txt").mkString should be("foobarbaz")
      } finally new java.io.File("/tmp/test-file.txt").delete()
    }
  }
}
