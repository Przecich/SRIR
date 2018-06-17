import scala.collection.mutable.ArrayBuffer

/** Code repository which stores code int internal buffer */
class CodeRepository {
  /** ArrayBuffer of stored codes */
  var codes = new ArrayBuffer[String]()

  /** Finds the most similar code in repository
    * @param code Referential code used in search
    * @return None if repository is empty and pair of code as string and difference summary
    **/
  def getMostSimilarCode(code: String): Option[(String, DifferenceSummary)] = {
    if (codes.isEmpty) {
      None
    } else {
      val summary: ArrayBuffer[(String, DifferenceSummary)] = codes.map(repoCode => (repoCode, DifferenceUtility.calculateDifference(code, repoCode)))
      val sorted = summary.minBy(tuple => tuple._2.absoluteDifference)
      Some(sorted)
    }
  }
  /** Adds code to internal buffer
    * @param newCode Code to be added to repository
    * */
  def addCodeToRepository(newCode: String): Unit = {
    codes += newCode
  }
}
