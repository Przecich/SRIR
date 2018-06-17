/** Class represents summary of differences of two given codes
  * @param inserted Inserted characters count
  * @param deleted Deleted characters count
  * @param levenshteinDistance Distance between two codes using Levenshtein's metric
  * */
class DifferenceSummary(val inserted: Int, val deleted: Int, val levenshteinDistance: Int){
  /** Calculates absolute difference of codes which is sum of inserted and deleted characters
    * @return Sum of inserted and deleted characters*/
  def absoluteDifference: Int = inserted + deleted
}
