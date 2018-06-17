import difftool.diff_match_patch
import scala.collection.JavaConverters._

/** Provides utility to calculate difference between codes and format summary as string */
object DifferenceUtility {
  /** Calculates difference between given codes
    * @param code1 Referential code
    * @param code2 Code that's being compared to code1
    * @return Summary of differences between codes*/
  def calculateDifference(code1: String, code2: String): DifferenceSummary = {
    val dmp = new diff_match_patch
    val differenceList = dmp.diff_main(code1, code2)
    val distance = dmp.diff_levenshtein(differenceList)

    val diffVector: Vector[diff_match_patch.Diff] = differenceList.asScala.toVector

    val addedCount = diffVector
      .filter(diff => diff.operation == diff_match_patch.Operation.INSERT)
      .foldLeft(0)((accumulator, diff) => accumulator + diff.text.length)

    val removedCount = diffVector
      .filter(diff => diff.operation == diff_match_patch.Operation.DELETE)
      .foldLeft(0)((accumulator, diff) => accumulator + diff.text.length)

    new DifferenceSummary(addedCount, removedCount, distance)
  }
/** Formats result of the most similar code search to string
  * @param result Optional tuple of the most similar code and summary of comparison
  * @return Formatted summary as string*/
  def summaryPrinter(result: Option[(String, DifferenceSummary)]): String = result match {
    case None => "Analiza kodu: Brak kodu w repozytorium"
    case Some((code: String, summary: DifferenceSummary)) => "Analiza kodu:\nDodanych znakow: " + summary.inserted + "\nUsunietych znakow: " + summary.deleted + "\nOdleglosc Levenshteina: " + summary.levenshteinDistance + "\nLoC: " + code.count(_ == '\n').+(1) + "\nCode preview: " + code.take(10) + "..."
  }
}
