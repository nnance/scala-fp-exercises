import org.scalatest._
import exercises.optionsLib

class OptionsSpec extends FlatSpec with Matchers {
  val someValue: Option[String] = Some("I am wrapped in something")
  someValue should be(Some("I am wrapped in something"))

  val emptyValue: Option[String] = None
  emptyValue should be(None)

  // Using getOrElse we can provide a default value ("No value") when the optional argument (None) does not exist.
  val value1 = optionsLib.maybeItWillReturnSomething(true)
  val value2 = optionsLib.maybeItWillReturnSomething(false)

  value1 getOrElse "No value" should be("Found value")
  value2 getOrElse "No value" should be("No value")
  value2 getOrElse {
    "default function"
  } should be("default function")

  // Checking whether option has value
  value1.isEmpty should be(false)
  value2.isEmpty should be(true)

  // pattern matching example
  val patternValue: Option[Double] = Some(20.0)
  val matchedVal = patternValue match {
    case Some(v) ⇒ v
    case None ⇒ 0.0
  }
  matchedVal should be(20.0)

  val noValue: Option[Double] = None
  val matchedVal2 = noValue match {
    case Some(v) ⇒ v
    case None ⇒ 0.0
  }
  matchedVal2 should be(0.0)

  // this operation allows to map the inner value to a different type while preserving the option
  val number: Option[Int] = Some(3)
  val noNumber: Option[Int] = None
  val result1 = number.map(_ * 1.5)
  val result2 = noNumber.map(_ * 1.5)

  result1 should be(Some(4.5))
  result2 should be(None)

  // Another operation is fold. this operation will extract the value from the option, or provide a default if the value is None
  val numberFold: Option[Int] = Some(3)
  val noNumberFold: Option[Int] = None
  val result1Fold = numberFold.fold(0)(_ * 3)
  val result2Fold = noNumberFold.fold(0)(_ * 3)

  result1Fold should be(9)
  result2Fold should be(0)
}
