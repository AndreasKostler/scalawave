import sbt._

object Dependencies {
  val circeVersion = "0.8.0"
  val scalaTestVersion = "3.0.1"
  val fs2Version = "0.9.6"
  val catsVersion = "0.9.0"
  val monixVersion = "2.3.0"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  lazy val fs2 = "co.fs2" %% "fs2-core" % fs2Version
  lazy val monix = "io.monix" %% "monix" % monixVersion
  lazy val monixCats = "io.monix" %% "monix-cats" % monixVersion
  lazy val cats = "org.typelevel" %% "cats" % catsVersion
  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
}
