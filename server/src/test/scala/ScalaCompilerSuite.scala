import org.scalatest.FlatSpec

class ScalaCompilerSuite extends FlatSpec {
  it should "contain a string \"Compilation succeeded\" when a compilation indeed succeeds" in {
    assert(Compiler.compile("print('Test');").contains("Compilation succeeded"))
  }

  it should "contain a string \"Compilation failed\" when a compilation indeed fails" in {
    assert(Compiler.compile("wubba lubba dub dub").contains("Compilation failed"))
  }
}