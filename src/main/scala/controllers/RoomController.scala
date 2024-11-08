package controllers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.Room
import services.RoomService
import utils.JsonFormats._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.slf4j.LoggerFactory
import spray.json.RootJsonFormat

object RoomController {
  val logger = LoggerFactory.getLogger(getClass)
  implicit val roomFormat: RootJsonFormat[Room] = jsonFormat1(Room)

  def routes: Route = pathPrefix("rooms") {
    concat(
      post {
        entity(as[Room]) { room =>
          RoomService.addRoom(room) match {
            case Right(addedRoom) =>
              logger.info(s"Quarto '${addedRoom.roomId}' adicionado ao inventário.")
              complete((201, s"Quarto '${addedRoom.roomId}' foi adicionado ao inventário com sucesso!"))
            case Left(error) =>
              logger.error(s"Erro ao adicionar o quarto: $error")
              complete((400, s"Erro ao adicionar o quarto: $error"))
          }
        }
      },
      delete {
        path(Segment) { roomId =>
          RoomService.removeRoom(roomId) match {
            case Right(_) =>
              logger.info(s"Quarto '$roomId' removido do inventário.")
              complete((200, s"Quarto '$roomId' foi removido do inventário com sucesso."))
            case Left(error) =>
              logger.warn(s"Tentativa de remover quarto inexistente: '$roomId'.")
              complete((404, s"Erro: Quarto '$roomId' não encontrado ou já foi removido."))
          }
        }
      }
    )
  }
}
