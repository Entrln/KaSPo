package com.entrln.model


/**
 *
 * @param userid   : A unique identifier assigned to each user. It helps in tracking the activities of a specific user across multiple sessions.
 * @param clientid :  A unique identifier assigned to each browser or device.
 */
case class UserData(userid: String, clientid: String)
