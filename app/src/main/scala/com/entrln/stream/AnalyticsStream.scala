package com.entrln.stream

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import com.entrln.model.analytics.AnalyticsActivity
import com.entrln.stream.Implicits._
import io.circe
import io.circe.parser._
import org.apache.kafka.clients.consumer.ConsumerRecord
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


//
object AnalyticsStream extends App {
  private val db: Database = Database.forConfig("kaspodb")

  private implicit val system: ActorSystem = ActorSystem("KafkaToPostgresStream")
  implicit val executionContext: ExecutionContext = system.dispatcher
  private implicit val materializer: Materializer = ActorMaterializer()

  private val kafkaSource: Source[ConsumerRecord[String, String], Consumer.Control] = KaspoSource.createSourceFor(system)
  private val slickSink: Sink[Option[AnalyticsActivity], Future[Done]] = KaspoSink.createSinkFor(system, db)
  private val conversionFlow = (record: ConsumerRecord[String, String]) => {
    val key: String = record.key()
    val value: String = record.value() // json
    val conversion: Either[circe.Error, AnalyticsActivity] = decode[AnalyticsActivity](value)
    if (conversion.isLeft) println("Unable to convert: " + value + " to AnalyticsActivity.")
    // return an Option and let the Sink handle it.
    conversion.toOption
  }

  // start the stream.
  val stream: Future[Done] = kafkaSource.map(record => conversionFlow(record)).runWith(slickSink)
  stream onComplete {
    case Success(_) =>
      db.close()
      system.terminate()
    case Failure(exception) => println("Error: " + exception)
  }
}
