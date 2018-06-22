
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

/**
  * Main class of the application
  *
  */
object ClientApp {

  private val  logger = new Logger

  /**
    * Startup method
    * @param args standart arguments
    *
    */
  def main(args: Array[String]) = {
    logger.info("Client started");
    val client = new Client(getConfig())
    client.run();
  }

  /**
    * getting config from conf.properties file
    * @return tuple containing connection data
    *
    */
  def getConfig(): (String, String) = {
    val prop = new Properties()
    var input: FileInputStream = null

    var ip: String = "localhost"
    var port: String = "8080"
    try {
      input = new FileInputStream("config.properties")
      prop.load(input)

      ip = prop.getProperty("serverIp")
      port = prop.getProperty("port")

    } catch {
      case ex: IOException =>
        ex.printStackTrace()
        logger.error("Cannot load conf file")
    } finally if (input != null) try
      input.close()
    catch {
      case e: IOException =>
        e.printStackTrace()
        logger.error("Cannot close conf file")
    }

    return (ip, port);
  }


}