import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App with Directives with StrictLogging {
  implicit val as: ActorSystem = ActorSystem("js-eval")
  implicit val ec: ExecutionContext = as.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()
  var repo = new CodeRepository

  val endpoints =
    path("endpoint") {
      handleWebSocketMessages {
        Flow.fromFunction {
          case msg: TextMessage =>
            val text = msg.getStrictText
            val similar = repo.getMostSimilarCode(text)
            val appendedMsg = DifferenceUtility.summaryPrinter(similar)
            repo.addCodeToRepository(text)

            TextMessage(Compiler.compile(text) + "\n\n" + appendedMsg)
          case _ =>
            TextMessage("")
        }
      }
    }

  Http().bindAndHandle(endpoints, "localhost", 9321).onComplete {
    case Success(address) =>
      logger.info("Successfully bound to {}", address)
    case Failure(ex) =>
      logger.error(s"Couldn't bind: {}", ex)
  }
}