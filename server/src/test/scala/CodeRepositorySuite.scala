import org.scalatest.FlatSpec

class CodeRepositorySuite extends FlatSpec {
  "A repository" should "be empty after construction" in {
    val repo = new CodeRepository
    assert(repo.codes.isEmpty)
  }

  it should "not be empty after code insertion" in {
    var repo = new CodeRepository
    repo.addCodeToRepository("terminate();")
    assert(repo.codes.nonEmpty)
  }

  it should "return None if requested a most similar code while being empty" in {
    val repo = new CodeRepository
    assert(repo.getMostSimilarCode("guanabanakumkwat();") == None)
  }

  it should "return function(a: number) when asked for most similar code" in {
    var repo = new CodeRepository
    repo.addCodeToRepository("calculateCalculation(){ reutnr 0;}")
    repo.addCodeToRepository("function calculateCalculation(a: number){ reutnr 0;}")
    repo.addCodeToRepository("function(a: number)")
    assert(repo.getMostSimilarCode("function(b: number)").get._1 == "function(a: number)")
  }
  it should "say that distance is 1 when asked for similar code" in {
    var repo = new CodeRepository
    repo.addCodeToRepository("calculateCalculation(){ reutnr 0;}")
    repo.addCodeToRepository("function calculateCalculation(a: number){ reutnr 0;}")
    repo.addCodeToRepository("function(a: number)")
    assert(repo.getMostSimilarCode("function(b: number)").get._2.levenshteinDistance == 1)
  }

  it should "return the same code when presented the same code as previously inserted" in {
    var repo = new CodeRepository
    repo.addCodeToRepository("calculateCalculation(){ reutnr 0;}")
    repo.addCodeToRepository("function calculateCalculation(a: number){ reutnr 0;}")
    repo.addCodeToRepository("function(b: number)")
    assert(repo.getMostSimilarCode("function(b: number)").get._1 == "function(b: number)")
  }

  it should "show difference equal 0 if presented the same code as previously inserted" in {
    var repo = new CodeRepository
    repo.addCodeToRepository("calculateCalculation(){ reutnr 0;}")
    repo.addCodeToRepository("function calculateCalculation(a: number){ reutnr 0;}")
    repo.addCodeToRepository("function(b: number)")
    val result = repo.getMostSimilarCode("function(b: number)").get
    assert(result._2.levenshteinDistance == 0 && result._2.deleted == 0 && result._2.inserted == 0 && result._2.absoluteDifference == 0 )
  }
}
