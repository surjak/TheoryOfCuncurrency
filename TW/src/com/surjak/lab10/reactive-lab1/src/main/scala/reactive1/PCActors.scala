package reactive1

/**
 * Created by surjak on 12.01.2021
 */

import akka.actor.Actor
import akka.actor.Stash
import akka.actor.ActorRef
import akka.actor.Props
import akka.event.LoggingReceive
import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Await

object PC {

  case class Init()

  case class Put(x: Long)

  case class Get()

  case class ProduceDone()

  case class ConsumeDone(x: Long)

}

class Producer(name: String, buf: ActorRef) extends Actor {

  import PC._

  def receive = LoggingReceive {
    case Init => buf ! Put(Math.random().longValue())
    case ProduceDone => println(s"$name: Produced value"); buf ! Put((Math.random() * 1000).longValue())
  }

}

class Consumer(name: String, buf: ActorRef) extends Actor {

  import PC._

  def receive = LoggingReceive {
    case Init => buf ! Get
    case ConsumeDone(x) => println(s"$name: Consumed $x"); buf ! Get
  }

}


class Buffer(n: Int) extends Actor with Stash {

  import PC._

  private val buf = new Array[Long](n)
  private var count = 0

  def receive = LoggingReceive {
    case Put(x) if (count < n) => sender ! ProduceDone; buf(count) = x; count += 1; println(s"Buffer size $count"); unstashAll();
    case Get if (count > 0) => sender ! ConsumeDone(buf(count - 1)); count -= 1; println(s"Buffer size $count"); unstashAll();
    case x => println(s"Stashing actor $x"); stash()
  }
}


object ProdConsMain extends App {

  import PC._

  val system = ActorSystem("ProdKons")
  private val bufferSize = 3

  val buffer = system.actorOf(Props(new Buffer(bufferSize)), "buffer")
  val consumer1 = system.actorOf(Props(new Consumer(s"consumer 1", buffer)), "consumer1")
  val consumer2 = system.actorOf(Props(new Consumer(s"consumer 2", buffer)), "consumer2")
  val consumer3 = system.actorOf(Props(new Consumer(s"consumer 3", buffer)), "consumer3")
  val consumer4 = system.actorOf(Props(new Consumer(s"consumer 4", buffer)), "consumer4")
  val consumer5 = system.actorOf(Props(new Consumer(s"consumer 5", buffer)), "consumer5")
  val producer1 = system.actorOf(Props(new Producer(s"producer 1", buffer)), "producer1")
  val producer2 = system.actorOf(Props(new Producer(s"producer 2", buffer)), "producer2")
  val producer3 = system.actorOf(Props(new Producer(s"producer 3", buffer)), "producer3")
  val producer4 = system.actorOf(Props(new Producer(s"producer 4", buffer)), "producer4")
  val producer5 = system.actorOf(Props(new Producer(s"producer 5", buffer)), "producer5")

  consumer1 ! Init
  consumer2 ! Init
  consumer3 ! Init
  consumer4 ! Init
  consumer5 ! Init
  producer1 ! Init
  producer2 ! Init
  producer3 ! Init
  producer4 ! Init
  producer5 ! Init


  Await.result(system.whenTerminated, Duration.Inf)
}



