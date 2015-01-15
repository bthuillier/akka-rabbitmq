package com.gilwath.rabbitmq

import akka.actor.Props
import akka.stream.actor.ActorSubscriberMessage.OnNext
import akka.stream.actor.{OneByOneRequestStrategy, ActorSubscriber, RequestStrategy}
import akka.stream.scaladsl.Sink
import com.rabbitmq.client.Channel


class RabbitMqSubscriber(val channel: Channel, val queue: String, val exchange: Option[String]) extends ActorSubscriber {
  override protected def requestStrategy: RequestStrategy = OneByOneRequestStrategy

  override def receive: Receive = {
    case OnNext(RabbitMqMsg(body)) => channel.basicPublish(exchange.getOrElse(""), queue, null, body.getBytes)
  }
}

object RabbitMqSubscriber {
  private def props(channel: Channel, queue: String, exchange: Option[String] = None): Props =
    Props(classOf[RabbitMqSubscriber], channel, queue, exchange)

  def sink(channel: Channel, queue: String, exchange: Option[String] = None): Sink[RabbitMqMsg] =
    Sink(props(channel, queue, exchange))
}
