class DifferenceSummary(val inserted: Int, val deleted: Int, val levenshteinDistance: Int){
  def absoluteDifference: Int = inserted + deleted

}
