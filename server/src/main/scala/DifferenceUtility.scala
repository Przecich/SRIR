import difftool.diff_match_patch
import scala.collection.JavaConverters._

object DifferenceUtility {
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

  def summaryPrinter(result: Option[(String, DifferenceSummary)]): String = result match {
    case None => "Analiza kodu: Brak kodu w repozytorium"
    case Some((code: String, summary: DifferenceSummary)) => "Analiza kodu:\nDodanych znakow: " + summary.inserted + "\nUsunietych znakow: " + summary.deleted + "\nOdleglosc Levenshteina: " + summary.levenshteinDistance + "\nLoC: " + code.count(_ == '\n').+(1) + "\nCode preview: " + code.take(10) + "..."
  }
}
