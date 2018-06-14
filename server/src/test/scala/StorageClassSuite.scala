import org.scalatest.FlatSpec

class StorageClassSuite extends FlatSpec {
  "A DifferenceSummary object" should "correctly assign values when passed to constructor" in {
    val obj = new DifferenceSummary(1, 2, 3)
    assert(obj.inserted == 1 && obj.deleted == 2 && obj.levenshteinDistance ==3)
  }

  it should "correctly compute value od absolute changes which is sum od inserted and deleted" in {
    val obj = new DifferenceSummary(12, 2, 3)
    assert(obj.absoluteDifference == 14)
  }
}
