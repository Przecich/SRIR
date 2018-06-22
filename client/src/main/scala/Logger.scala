import java.util.Calendar

/**
  * Logger class, handling basic instrumentation
  *
  */
class Logger {

    /**
    * Generate info log
    *@param message message to be printed
    */
    def info(message:String) {
      printMessage(message,"INFO")
    }

    /**
    * Generate error log
    *@param message message to be printed
    */
    def error(message:String) {
      printMessage(message,"ERROR")
    }

    private def printMessage(message:String, logType:String): Unit ={
      println("["+getTime+"]"+logType+":"+message)
    }

    private def getTime ={
      Calendar.getInstance.getTime
    }
}
