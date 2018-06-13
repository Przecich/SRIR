import scala.collection.mutable.ArrayBuffer

class CodeRepository {
  var codes = new ArrayBuffer[String]()

  def getMostSimilarCode(code: String): Option[(String, DifferenceSummary)] = {
    if (codes.isEmpty) {
      None
    } else {
      val summary: ArrayBuffer[(String, DifferenceSummary)] = codes.map(repoCode => (repoCode, DifferenceCalculator.calculate(code, repoCode)))
      val sorted = summary.minBy(tuple => tuple._2.absoluteDifference)
      Some(sorted)
    }
  }

  def addCodeToRepository(newCode: String): Unit = {
    codes += newCode
  }
}
