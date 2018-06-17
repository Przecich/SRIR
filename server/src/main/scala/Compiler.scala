import javax.script.{ScriptEngine, ScriptEngineManager}
import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object Compiler {
  private final val nashorn: ScriptEngine = new ScriptEngineManager().getEngineByName("nashorn")

  def compile(sourceCode: String): String = {
    val compilationErrorMsg = "Compilation failed"
    val compilationSuccessMsg = "Compilation succeeded"
    val result = Try(nashorn.eval(sourceCode))

    result match {
      case Success(null) => compilationSuccessMsg
      case Success(res: ScriptObjectMirror) => compilationSuccessMsg + ":\n " + res.asScala.toString
      case Failure(ex) => compilationErrorMsg + ":\n " + ex.getMessage
      case _ => compilationErrorMsg
    }
  }
}
