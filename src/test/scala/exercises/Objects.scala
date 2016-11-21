import org.scalatest._
import exercises._

class ObjectsSpec extends FlatSpec with Matchers {
  Greeting.english should be("Hi")
  Greeting.espanol should be("Hola")
  Greeting.deutsch should be("Hallo")
  Greeting.magyar should be("Szia")


  val x = Greeting
  val y = x

  x eq y should be(true) //Reminder: eq checks for reference

  val z = Greeting

  x eq z should be(true)

  Movie.academyAwardBestMoviesForYear(1932).get.name should be("Grand Hotel")

  val clark = new Person("Clark Kent", "Superman")
  val peter = new Person("Peter Parker", "Spiderman")
  val bruce = new Person("Bruce Wayne", "Batman")
  val diana = new Person("Diana Prince", "Wonder Woman")

  Person.showMeInnerSecret(clark) should be("Superman")
  Person.showMeInnerSecret(peter) should be("Spiderman")
  Person.showMeInnerSecret(bruce) should be("Batman")
  Person.showMeInnerSecret(diana) should be("Wonder Woman")
}
