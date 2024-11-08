package dao

import models.{Reservation, ReservationTable}
import slick.jdbc.H2Profile.api._
import scala.concurrent.{ExecutionContext, Future}
import java.time.LocalDateTime 

class ReservationDAO(db: Database)(implicit ec: ExecutionContext) {
  private val reservations = ReservationTable.reservations

  def addReservation(reservation: Reservation): Future[Long] = {
    db.run((reservations returning reservations.map(_.id)) += reservation)
  }

  import java.time.{LocalDateTime, LocalTime}

def getReservationsForDate(date: LocalDateTime): Future[Seq[Reservation]] = {
 
  val startOfDay = LocalDateTime.of(date.toLocalDate, LocalTime.MIN)  
  val endOfDay = LocalDateTime.of(date.toLocalDate, LocalTime.MAX)    

  db.run(
    reservations.filter(res =>
      res.startDate >= startOfDay && res.startDate <= endOfDay
    ).result
  )
}
}
