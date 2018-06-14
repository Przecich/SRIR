import difftool.diff_match_patch
import org.scalatest.FlatSpec
import scala.collection.JavaConverters._

class DiffMatchPatchSuite extends FlatSpec {
  "Diff checker" should "be initialized with timeout value 1s" in {
    val a = new diff_match_patch
    assert(a.Diff_Timeout == 1.0f)
  }

  it should "return 0 differences(only equal parts) given the same code" in {
    var a =  new diff_match_patch
    val diff = a.diff_main("pomocy(0x80h);" , "pomocy(0x80h);" ).asScala.toVector
    assert(diff.forall(d => d.operation == diff_match_patch.Operation.EQUAL))
  }

  it should "only indicate deletion and equal parts if 10 letters have been removed from code" in {
    var a =  new diff_match_patch
    val diff = a.diff_main("pomocy(0x80h); podwodnyNurek.NurkujTeraz();" , "pomocy(0x80h); podwodnyujTeraz()" ).asScala.toVector
    assert(diff.forall(d => d.operation == diff_match_patch.Operation.DELETE || d.operation == diff_match_patch.Operation.EQUAL))
  }

  it should "throw exception when one of the codes is null" in {
    assertThrows[IllegalArgumentException] {
      (new diff_match_patch).diff_main(null, null)
    }
  }
}
