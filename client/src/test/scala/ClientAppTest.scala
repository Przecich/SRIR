 import org.scalatest.FlatSpec


  /**
    * Tests for client class
    *
    */
  class ClientAppTest extends FlatSpec {
    val client = new Client("localhost", "9321")

    "getConfig " should "return correct number of" in {
      val data = ClientApp.getConfig();
      assert(data!=null)
      assert(!data._1.isEmpty)
      assert(!data._2.isEmpty)
    }
  }

