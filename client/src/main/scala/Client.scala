
import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.Future

/**
  * Client class handling connection and displaying response
  *
  */
class Client( connectionData:(String,String)) {

  private val pattern = """\s(\w+.txt)$""".r
  private val  logger = new Logger

  private val printSink: Sink[Message, Future[Done]] =
    Sink.foreach {
      case message: TextMessage.Strict =>
        println(message.text+'\n')
    }


  /**
    * Client class handling connection and displaying response
    *
    */
  def run(): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher


    val command = getCommand()
    val sourceCode = getSourceCode(command)


    val helloSource = Source.single(TextMessage(sourceCode))

    val webSocketFlow =
      Http().webSocketClientFlow(WebSocketRequest("ws://" + connectionData._1 + ":"
        + connectionData._2 + "/endpoint"))

    val (upgradeResponse, closed) =
      helloSource
        .viaMat(webSocketFlow)(Keep.right)
        .toMat(printSink)(Keep.both)
        .run()

    val connected = upgradeResponse.map { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        run()
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }

  }

  private def getCommand(): String = {
    scala.io.StdIn.readLine("> ")
  }

  def trimCommand(command: String): String = {
    pattern.findFirstIn(command).getOrElse(';').toString
  }

  def getSourceCode(command: String): String = {
    if (command.startsWith("/f")) {
      val code = scala.io.Source.fromFile(trimCommand(command).trim).getLines().mkString
      if (code.isEmpty) {
        logger.error("Couldn't load file")
        return "NO command"
      } else {
        code
      }
    }
    else
      command
  }
}
