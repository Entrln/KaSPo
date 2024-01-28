package com.entrln


/**
 *
 * --------------------------------------------------------
 * 1: Analytics data streaming and persistence.
 * --------------------------------------------------------
 * Kafka -> Akka Stream -> Postgres.
 *
 */
object App {
  def main(args: Array[String]): Unit = {
    // 1- Ensure docker compose is run before starting the stream.
    // 2- Run the stream              => run AnalyticsStream.
    // 3- Publish messages to kafka   => run DataProducer.
  }
}
