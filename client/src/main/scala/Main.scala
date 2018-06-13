import akka.actor.ActorSystem
import akka.{Done, NotUsed}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws._
import akka.actor.ActorSystem
import akka.Done
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws._


import scala.concurrent.Future

object SingleWebSocketRequest {



  def main(args: Array[String]) = {
    println("Client started");
    val address = scala.io.StdIn.readLine("Type server address ");
    Client.run(address);
  }


}