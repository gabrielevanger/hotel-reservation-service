package models

import java.time.LocalDateTime

case class Reservation(
  id: Option[Long] = None,
  roomId: String,
  guestId: String,
  startDate: LocalDateTime,
  endDate: LocalDateTime
)
