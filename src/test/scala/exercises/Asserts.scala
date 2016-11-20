import org.scalatest._

class AssertSpec extends FlatSpec with Matchers {
  "true" should "be true" in {
    true should be(true)
  }
  "v1" should "be 4" in {
    val v1 = 4
    v1 shouldEqual 4
  }
}
