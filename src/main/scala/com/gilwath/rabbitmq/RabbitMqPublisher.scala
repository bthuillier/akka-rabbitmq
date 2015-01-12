package com.gilwath.rabbitmq

import akka.actor.Props
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{Cancel, Request}
import com.rabbitmq.client.QueueingConsumer
import com.rabbitmq.client.QueueingConsumer.Delivery


class RabbitMqPublisher(val queue: QueueingConsumer) extends ActorPublisher[Delivery]{
  override def receive: Receive = {
    case Request(x) =>
      deliver(x)
    case Cancel =>
      context.stop(self)
  }

  def deliver(x: Long): Unit = {
    if(x > 0) {
      onNext(queue.nextDelivery())
      deliver(x - 1)
    }
  }
}

object RabbitMqPublisher {
  def props(queue: QueueingConsumer): Props = {
    Props(classOf[RabbitMqPublisher], queue)
  }
}
