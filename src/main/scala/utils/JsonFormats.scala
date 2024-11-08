package utils

import spray.json.{DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}
import spray.json.DefaultJsonProtocol._ 
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import models.Reservation

object JsonFormats {


  implicit val localDateTimeFormat: JsonFormat[LocalDateTime] = new JsonFormat[LocalDateTime] {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    
    def write(dateTime: LocalDateTime): JsValue = JsString(dateTime.format(formatter))
    
    def read(json: JsValue): LocalDateTime = json match {
      case JsString(dateString) =>
        try {
          LocalDateTime.parse(dateString, formatter)
        } catch {
          case e: Exception => throw DeserializationException("Expected LocalDateTime formatted string", e)
        }
      case _ =>
        throw DeserializationException("Expected LocalDateTime formatted string")
    }
  }


  implicit val reservationFormat: RootJsonFormat[Reservation] = jsonFormat5(Reservation)
}
