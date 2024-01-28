package com.entrln.stream

import com.entrln.model.analytics.AnalyticsActivity
import io.circe.{Decoder, HCursor}

/**
 * todo: Model.analytics.AnalyticsActivity would be a better home for this.
 */
object Implicits {
  implicit val analyticsActivityDecoder: Decoder[AnalyticsActivity] = (c: HCursor) =>
    for {
      userid <- c.downField("userid").as[String]
      clientid <- c.downField("clientid").as[String]
      activityType <- c.downField("activityType").as[String]
      bucket <- c.downField("bucket").as[String]
    } yield AnalyticsActivity(userid, clientid, activityType, bucket)
}
