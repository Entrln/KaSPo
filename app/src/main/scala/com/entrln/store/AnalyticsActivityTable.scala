package com.entrln.store

import com.entrln.model.analytics.AnalyticsActivity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

/**
 *
 * @param tag : .
 */
class AnalyticsActivityTable(tag: Tag) extends Table[AnalyticsActivity](tag, Some("entrln"), "analytics_activity") {
  def userid: Rep[String] = column[String]("userid")

  def clientid: Rep[String] = column[String]("clientid")

  def activityType: Rep[String] = column[String]("activity_type")

  def bucket: Rep[String] = column[String]("bucket")

  def * : ProvenShape[AnalyticsActivity] = (userid, clientid, activityType, bucket) <> (AnalyticsActivity.tupled, AnalyticsActivity.unapply)
}
