package services

import models.Reservation
import java.time.LocalDateTime

object ReservationService {
  private var reservations: List[Reservation] = List()

  def addReservation(reservation: Reservation): Either[String, Reservation] = {
    
    val conflict = reservations.exists { existingReservation =>
      existingReservation.roomId == reservation.roomId &&
      (
        (reservation.startDate.isBefore(existingReservation.endDate) && reservation.endDate.isAfter(existingReservation.startDate)) ||
        reservation.startDate.isEqual(existingReservation.startDate) ||
        reservation.endDate.isEqual(existingReservation.endDate)
      )
    }

    if (conflict) {
      Left("Conflict") 
    } else {
    
      val cleaningWindowViolation = reservations.exists { existingReservation =>
        existingReservation.roomId == reservation.roomId &&
        (
          reservation.startDate.isBefore(existingReservation.endDate.plusHours(4)) &&
          reservation.startDate.isAfter(existingReservation.endDate)
        )
      }

      if (cleaningWindowViolation) {
        Left("CleaningWindow") 
      } else {
  
        reservations = reservation :: reservations
        Right(reservation)
      }
    }
  }

  def getReservationsForDate(date: LocalDateTime): List[Reservation] = {
    reservations.filter(res => res.startDate.toLocalDate == date.toLocalDate)
  }
}
