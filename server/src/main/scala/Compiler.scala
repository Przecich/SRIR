import javax.script.{ScriptEngine, ScriptEngineManager}
import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/** Provides the means to evaluate a source code at a runtime. */
object Compiler {
  /** The engine which is used to evaluate the source code. */
  private final val nashorn: ScriptEngine = new ScriptEngineManager().getEngineByName("nashorn")

  /**
    * Evaluates the specified source code and returns the evaluation result.
    *
    * The returned evaluation result will contain a precise compilation error message should the specified
    * source code be impossible to compile.
    *
    * @param sourceCode A source code to evaluate.
    * @return A message depicting the evaluation status.
    */
  def compile(sourceCode: String): String = {
    val compilationErrorMsg = "Compilation failed"
    val compilationSuccessMsg = "Compilation succeeded"
    val result = Try(nashorn.eval(sourceCode))

    result match {
      case Success(null) => compilationSuccessMsg
      case Success(res: ScriptObjectMirror) => compilationSuccessMsg + ":\n " + res.asScala.toString
      case Success(res) => compilationSuccessMsg + ":\n " + res.toString
      case Failure(ex) => compilationErrorMsg + ":\n " + ex.getMessage
    }
  }
}
