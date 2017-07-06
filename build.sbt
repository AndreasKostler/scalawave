import Dependencies._

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

lazy val compilerOptions = Seq(
  "-unchecked",
  "-deprecation",
  "-target:jvm-1.8",
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
  //"-Ywarn-unused"
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "scalawave",
      scalaVersion := "2.12.2",
      version      := "0.1.0-SNAPSHOT",
      scalacOptions := compilerOptions
    )),
    name := "Scalawave",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      monix,
      monixCats,
      cats
    ) ++ circe
  )

