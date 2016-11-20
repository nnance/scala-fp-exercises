import org.scalatest._
import exercises.ClassWithValParameter

class ClassesSpec extends FlatSpec with Matchers {
  "name" should "be Gandalf" in {
    val classWithValParameter = new ClassWithValParameter("Gandalf")
    classWithValParameter.name should be("Gandalf")
  }
}
