package exercises

object optionsLib {
  def maybeItWillReturnSomething(flag: Boolean): Option[String] = {
    if (flag) Some("Found value") else None
  }
}
