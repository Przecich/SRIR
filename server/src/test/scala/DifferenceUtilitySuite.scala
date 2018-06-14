import org.scalatest.FlatSpec

class DifferenceUtilitySuite extends FlatSpec {
  "A DifferenceUtility" should "calculate zero difference given the same code" in {
    val result = DifferenceUtility.calculateDifference(
      " data structure representing a diff is a Linked list of Diff objects:\n   * {Diff(Operation.DELETE, \"Hello\"), Diff(Operation.INSERT, \"Goodbye\"),\n   *  Diff(Operation.EQUAL, ",
      " data structure representing a diff is a Linked list of Diff objects:\n   * {Diff(Operation.DELETE, \"Hello\"), Diff(Operation.INSERT, \"Goodbye\"),\n   *  Diff(Operation.EQUAL, ")
    assert(result.absoluteDifference == 0 && result.levenshteinDistance == 0 && result.levenshteinDistance == 0)
  }

  it should "indicate that 1 letter has been inserted and 2 has been deleted when altered letter and removed space" in {
    val result = DifferenceUtility.calculateDifference("ture representing a diff is a Linked list o", "dure representinga diff is a Linked list o")
    assert(result.inserted == 1 && result.deleted == 2)
  }

  it should "print inserted 1, deleted 2, distance 2, correctyl format text, LoC 1 and show only 10 lettes of code" in {
    val result = DifferenceUtility.calculateDifference("ture representing a diff is a Linked list o", "dure representinga diff is a Linked list o")
    val expectedText = "Analiza kodu:\nDodanych znakow: 1\nUsunietych znakow: 2\nOdleglosc Levenshteina: 2\nLoC: 1\nCode preview: ture repre..."
    assert(expectedText == DifferenceUtility.summaryPrinter(Some(("ture representing a diff is a Linked list o", result))))
  }

  it should "print no code prompt when given Option of None" in {
    val result = DifferenceUtility.summaryPrinter(None)
    assert(result == "Analiza kodu: Brak kodu w repozytorium")
  }

  it should "thow IllegalArgumentException when one of arguments is null" in {
    assertThrows[IllegalArgumentException] {
      DifferenceUtility.calculateDifference("batat.atakuj()", null)
    }
  }

  it should "prompt 3 LoC when given a code with 2 new-line feeds" in {
    val result = DifferenceUtility.calculateDifference("little()\n tiny piece\n code", "little()\n tiny piece\n code")
    assert(DifferenceUtility.summaryPrinter(Some("little()\n tiny piece\n code", result)) contains "LoC: 3" )
  }
}
