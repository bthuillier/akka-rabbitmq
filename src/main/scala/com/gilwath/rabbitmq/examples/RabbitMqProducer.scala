package com.gilwath.rabbitmq.examples

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.gilwath.rabbitmq.{RabbitMqMsg, RabbitMqSubscriber}
import com.rabbitmq.client.ConnectionFactory


object RabbitMqProducer extends App {
  implicit val system: ActorSystem = ActorSystem("reactive-rabbitmq")
  implicit val mat = FlowMaterializer()

  val factory = new ConnectionFactory()
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val queue = List("foo", "bar")
  val sink: Sink[RabbitMqMsg] = RabbitMqSubscriber.sink(channel, "akka-stream")
  val source = Source(queue)

  source.map(m => RabbitMqMsg.apply(m)).to(sink).run()
}
