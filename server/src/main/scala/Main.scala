import javax.script.ScriptEngineManager

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import com.typesafe.scalalogging.StrictLogging
import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.concurrent.ExecutionContext
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object Main extends App with Directives with StrictLogging {
  implicit val as: ActorSystem = ActorSystem("js-eval")
  implicit val ec: ExecutionContext = as.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()
  var repo = new CodeRepository
  val nashorn = new ScriptEngineManager().getEngineByName("nashorn")

  val endpoints =
    path("endpoint") {
      handleWebSocketMessages {
        Flow.fromFunction {
          case msg: TextMessage =>
            val text = msg.getStrictText
            val similar = repo getMostSimilarCode text
            val codeAlreadyInRepo = similar.isDefined && similar.get._2.absoluteDifference == 0
            if(!codeAlreadyInRepo){
              repo addCodeToRepository text
              logger info "Added code to repository"
            }
            val appendedMsg = DifferenceUtility summaryPrinter similar

            val res = Try(nashorn.eval(text))
            res match {
              case Success(null) =>
                TextMessage("null" + "\n" + appendedMsg)
              case Success(result: ScriptObjectMirror) =>
                TextMessage(result.asScala.toString + "\n" + appendedMsg)
              case Success(result) =>
                TextMessage(result.toString + "\n" + appendedMsg)
              case Failure(ex) =>
                TextMessage(ex.getMessage)
            }
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