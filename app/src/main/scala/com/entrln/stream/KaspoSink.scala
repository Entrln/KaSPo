package com.entrln.stream

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import com.entrln.model.analytics.AnalyticsActivity
import com.entrln.store.AnalyticsActivityTable
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 *
 */
object KaspoSink {
  def createSinkFor(system: ActorSystem, db: Database): Sink[Option[AnalyticsActivity], Future[Done]] = {
    implicit val executionContext: ExecutionContext = system.dispatcher
    val analyticsActivityTableQuery: TableQuery[AnalyticsActivityTable] = TableQuery[AnalyticsActivityTable]
    // slick sink
    Sink.foreach[Option[AnalyticsActivity]] {
      case activity@Some(value) =>
        db.run(analyticsActivityTableQuery += value).onComplete {
          case Success(_) => println(s"insert succeeded: $activity")
          case Failure(ex) => println(s"insert failed: $activity, Error: $ex")
        }
      case None => println("Deserialization issue. Cannot create an AnalyticsActivity")
    }
  }
}
