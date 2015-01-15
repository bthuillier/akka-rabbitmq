package com.gilwath.rabbitmq.examples

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.gilwath.rabbitmq.RabbitMqPublisher
import com.rabbitmq.client.QueueingConsumer.Delivery
import com.rabbitmq.client.{ConnectionFactory, QueueingConsumer}


object RabbitMqConsumer extends App {
  implicit val system: ActorSystem = ActorSystem("reactive-rabbitmq")
  implicit val mat = FlowMaterializer()

  val factory = new ConnectionFactory()
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val queue =  new QueueingConsumer(channel)
  channel.basicConsume("akka-stream", true, queue)
  val source: Source[Delivery] = Source[Delivery](RabbitMqPublisher.props(queue))


  source.map(m => m.getBody).to(Sink.foreach(println)).run()
}
