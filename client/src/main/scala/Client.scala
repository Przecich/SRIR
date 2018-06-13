
import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.Future

object Client {
  val pattern = """\s(\w+.txt)$""".r

  val printSink: Sink[Message, Future[Done]] =
    Sink.foreach {
      case message: TextMessage.Strict =>
        println(message.text)
    }

  def run(address:String):Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher


    val webSocketFlow =
      Http().webSocketClientFlow(WebSocketRequest("ws://localhost:9321/endpoint"))

    val command = getCommand()
    val sourceCode = getSourceCode(command)


    val helloSource = Source.single(TextMessage(sourceCode))

    val (upgradeResponse, closed) =
      helloSource
        .viaMat(webSocketFlow)(Keep.right)
        .toMat(printSink)(Keep.both)
        .run()

    val connected = upgradeResponse.map { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        run(address)
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
    if (command.startsWith("/f"))
      scala.io.Source.fromFile(trimCommand(command).trim).getLines().mkString
    else
      command
  }
}
