import java.util

import org.scalatest._

/**
  * Tests for client class
  *
  */
class ClientTest extends FlatSpec {

  val client = new Client(("localhost","9321"))

  "getSourceCode " should "return a string" in {
    val command = client.getSourceCode("command")
    assert(command.equals("command"))
  }

  "trimCommand " should "return a string withot tag" in {
    val command = client.trimCommand("/f bla.txt")
    assert(command.equals(" bla.txt"))
  }

  "trimCommand " should "return an end line character" in {
    val command = client.trimCommand("/f command")
    val command2 = client.trimCommand("/fcommand")
    val command3 = client.trimCommand("/f command.")
    assert(command.equals(";"))
    assert(command2.equals(";"))
    assert(command3.equals(";"))
  }

}