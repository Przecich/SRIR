import javax.script.{ScriptEngine, ScriptEngineManager}
import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object ScalaCompiler {
  private final val nashorn: ScriptEngine = new ScriptEngineManager().getEngineByName("nashorn")

  def compile(sourceCode: String): String = {
    val result = Try(nashorn.eval(sourceCode))

    result match {
      case Success(null) => "null"
      case Success(res: ScriptObjectMirror) => res.asScala.toString
      case Failure(ex) => ex.getMessage
    }
  }
}
