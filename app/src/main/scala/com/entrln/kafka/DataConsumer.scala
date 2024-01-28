package com.entrln.kafka

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import java.util.Collections

/**
 * quick test to ensure Kafka is up. Part 1.
 *
 */
object DataConsumer extends KafkaConnection {
  def main(args: Array[String]): Unit = {
    val consumer = new KafkaConsumer[String, String](consumerProps)
    consumer.subscribe(Collections.singletonList(topic))

    try {
      while (true) {
        val records: ConsumerRecords[String, String] = consumer.poll(java.time.Duration.ofMillis(100))
        records.forEach(record => {
          println(s"Received message - Topic: ${record.topic()}, Partition: ${record.partition()}, Offset: ${record.offset()}, Key: ${record.key()}, Value: ${record.value()}")
        })
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      consumer.close()
    }
  }
}
