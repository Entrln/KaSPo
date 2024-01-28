package com.entrln.model

sealed trait ActivityType

final case class SessionOpen() extends ActivityType

final case class SessionClose() extends ActivityType

final case class PeriodicStateSnap() extends ActivityType
