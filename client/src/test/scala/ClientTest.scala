import java.util

import org.scalatest._

class ClientTest extends FlatSpec {

  "getSourceCode " should "return a string" in {
    val command = Client.getSourceCode("command")
    assert(command.equals("command"))
  }

  "trimCommand " should "return a string withot tag" in {
    val command = Client.trimCommand("/f bla.txt")
    assert(command.equals(" bla.txt"))
  }

  "trimCommand " should "return an end line character" in {
    val command = Client.trimCommand("/f command")
    val command2 = Client.trimCommand("/fcommand")
    val command3 = Client.trimCommand("/f command.")
    assert(command.equals(";"))
    assert(command2.equals(";"))
    assert(command3.equals(";"))
  }

}