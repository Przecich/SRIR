import javax.script.{ScriptEngine, ScriptEngineManager}
import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/** Provides the means to evaluate a JavaScript source code at a runtime. */
object Compiler {
  /** The engine which is used to evaluate the JavaScript source code. */
  private final val nashorn: ScriptEngine = new ScriptEngineManager().getEngineByName("nashorn")

  /**
    * Evaluates the specified JavaScript source code and returns the evaluation result.
    *
    * The returned evaluation result will contain a precise compilation error message should the specified
    * source code be impossible to compile.
    *
    * @param sourceCode A JavaScript source code to evaluate.
    * @return A message depicting the evaluation status.
    */
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
