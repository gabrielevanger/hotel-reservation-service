package models

import java.time.LocalDateTime
import slick.jdbc.H2Profile.api._
import java.sql.Timestamp

object ReservationTable {
  
  implicit val localDateTimeMapping = MappedColumnType.base[LocalDateTime, Timestamp](
    { localDateTime => Timestamp.valueOf(localDateTime) },
    { timestamp => timestamp.toLocalDateTime }
  )

  class Reservations(tag: Tag) extends Table[Reservation](tag, "RESERVATIONS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def roomId = column[String]("ROOM_ID")
  def guestId = column[String]("GUEST_ID")
  def startDate = column[LocalDateTime]("START_DATE")
  def endDate = column[LocalDateTime]("END_DATE")

  def * = (id.?, roomId, guestId, startDate, endDate) <> ((Reservation.apply _).tupled, Reservation.unapply)
}
  val reservations = TableQuery[Reservations]
}
