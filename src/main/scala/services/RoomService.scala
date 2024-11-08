package services

import models.Room

object RoomService {
  private var rooms: Map[String, Room] = Map()

  def addRoom(room: Room): Either[String, Room] = {
    if (rooms.contains(room.roomId)) Left("Quarto já existe")
    else {
      rooms = rooms + (room.roomId -> room)
      Right(room)
    }
  }

  def removeRoom(roomId: String): Either[String, Unit] = {
    if (rooms.contains(roomId)) {
      rooms = rooms - roomId 
      Right(())
    } else Left("Quarto não encontrado")
  }
}
