package com.entrln.stream

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer


/**
 *
 */
object KaspoSource {
  def createSourceFor(system: ActorSystem): Source[ConsumerRecord[String, String], Consumer.Control] = {
    val consumerSettings: ConsumerSettings[String, String] = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9094")
      .withGroupId("group60")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    // return
    Consumer.plainSource(consumerSettings, Subscriptions.topics("testopic"))
  }
}
