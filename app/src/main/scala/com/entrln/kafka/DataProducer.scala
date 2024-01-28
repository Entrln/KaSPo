package com.entrln.kafka

import com.entrln.model.analytics.AnalyticsActivity
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import io.circe.{Encoder, Json}
import io.circe.syntax._

import java.util.UUID

object data {
  def get(): Seq[(String, AnalyticsActivity)] = {
    Seq(
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "c1315ca009b1", activityType = "page_view", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "c1315ca009b1", activityType = "link_click", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "c1315ca009b1", activityType = "page_view", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "c1315ca009b1", activityType = "scroll_up", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "pause", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "page_view", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "link_click", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "link_click", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "pause", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "purchase", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "close", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "page_view", bucket = "[{k, v}]")),
      (UUID.randomUUID().toString, AnalyticsActivity(userid = "b7dcf030", clientid = "9421dc509806", activityType = "periodic_timestamp", bucket = "[{k, v}]")),
    )
  }
}

/**
 * quick test to ensure Kafka is up. Part 2.
 *
 */
object DataProducer extends KafkaConnection {
  def main(args: Array[String]): Unit = {
    // Create a Kafka producer
    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](producerProps)
    //
    implicit val analyticsActivityEncoder: Encoder[AnalyticsActivity] = (a: AnalyticsActivity) =>
      Json.obj(
        "userid" -> Json.fromString(a.userid),
        "clientid" -> Json.fromString(a.clientid),
        "activityType" -> Json.fromString(a.activityType),
        "bucket" -> Json.fromString(a.bucket)
      )

    // publish messages
    data.get().foreach(pair => {
      try {
        // a GUID as key.
        val key: String = pair._1
        val value: AnalyticsActivity = pair._2
        // publish json...
        val jsonValue: String = value.asJson.noSpaces
        val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, key, jsonValue)
        println(s"sending record to broker...")
        val metadata: RecordMetadata = producer.send(record).get()
        println(s"Record sent to topic: ${metadata.topic()}, partition: ${metadata.partition()}, offset: ${metadata.offset()}")
      } catch {
        case e: Exception => e.printStackTrace()
      }
    })

    // close producer.
    producer.close()
  }
}