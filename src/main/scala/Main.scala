import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import controllers.{RoomController, ReservationController}
import scala.concurrent.ExecutionContextExecutor

import scala.util.{Failure, Success}

object Main extends App {
  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "hotel-booking-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val corsSettings = CorsSettings.defaultSettings


  implicit def myExceptionHandler: ExceptionHandler = 
    ExceptionHandler {
      case e: Exception =>
        extractUri { uri =>
          println(s"Erro ao processar requisição para $uri: ${e.getMessage}")
          complete(StatusCodes.InternalServerError, "Erro interno do servidor.")
        }
    }

  val routes: Route = handleExceptions(myExceptionHandler) {
    cors(corsSettings) {
      concat(
        RoomController.routes,
        ReservationController.routes
      )
    }
  }

  Http().newServerAt("localhost", 8080).bind(routes).onComplete {
    case Success(binding) =>
      println(s"Servidor online em http://${binding.localAddress.getHostString}:${binding.localAddress.getPort}/")
    case Failure(ex) =>
      println(s"Falha ao iniciar o servidor: ${ex.getMessage}")
      system.terminate()
  }
}
