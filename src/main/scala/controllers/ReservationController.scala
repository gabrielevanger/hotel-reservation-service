package controllers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import models.Reservation
import services.ReservationService
import utils.JsonFormats._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import org.slf4j.LoggerFactory
import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._

object ReservationController {
  val logger = LoggerFactory.getLogger(getClass)
  implicit val reservationFormat: RootJsonFormat[Reservation] = jsonFormat5(Reservation)
  private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

  def routes: Route = pathPrefix("reservations") {
    concat(
      post {
        entity(as[Reservation]) { reservation =>
          if (reservation.roomId.isEmpty || !reservation.roomId.matches("[a-zA-Z0-9]+")) {
            logger.warn("Tentativa de criar reserva com 'roomId' inválido.")
            complete(StatusCodes.BadRequest, JsObject("error" -> JsString("O 'roomId' deve ser alfanumérico e não pode estar vazio.")))
          } else if (reservation.startDate.isAfter(reservation.endDate)) {
            logger.warn("Tentativa de criar reserva com data de início posterior à data de fim.")
            complete(StatusCodes.BadRequest, JsObject("error" -> JsString("A data de início não pode ser posterior à data de fim.")))
          } else {
            ReservationService.addReservation(reservation) match {
              case Right(addedReservation) =>
                logger.info(s"Reserva criada para o quarto '${addedReservation.roomId}'.")
                complete(StatusCodes.Created, JsObject("message" -> JsString(s"Reserva para o quarto '${addedReservation.roomId}' foi criada com sucesso.")))
              case Left("Conflict") =>
                complete(StatusCodes.Conflict, JsObject("error" -> JsString(s"Conflito de reserva para o quarto '${reservation.roomId}' no período solicitado.")))
              case Left("CleaningWindow") =>
                complete(StatusCodes.Conflict, JsObject("error" -> JsString(s"O quarto '${reservation.roomId}' requer uma janela de limpeza de 4 horas antes de novas reservas.")))
              case Left(error) =>
                logger.error(s"Erro ao criar reserva: $error")
                complete(StatusCodes.InternalServerError, JsObject("error" -> JsString("Erro interno do servidor. Por favor, tente novamente mais tarde.")))
            }
          }
        }
      },
      get {
        path("occupancy" / Segment) { dateStr =>
          val date = LocalDate.parse(dateStr, dateTimeFormatter)
          val reservations = ReservationService.getReservationsForDate(date.atStartOfDay())
          if (reservations.isEmpty) {
            complete(StatusCodes.OK, JsObject("message" -> JsString(s"Não há reservas para o dia ${date}.")))
          } else {
            val reservationDetails = reservations.map { res =>
              JsObject(
                "roomId" -> JsString(res.roomId),
                "guestId" -> JsString(res.guestId),
                "startDate" -> JsString(res.startDate.toString),
                "endDate" -> JsString(res.endDate.toString)
              )
            }
            val responseJson = JsObject(
              "date" -> JsString(date.toString),
              "reservations" -> JsArray(reservationDetails.toVector)
            )
            complete(StatusCodes.OK, responseJson)
          }
        }
      }
    )
  }
}
