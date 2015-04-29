name := "reactive-programming"

version := "1.0.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-async"     % "0.9.2",
  "com.typesafe.akka"      %% "akka-actor"      % "2.3.9",
  "org.scalatest"          %% "scalatest"       % "2.2.4"  % Test,
  "org.scalacheck"         %% "scalacheck"      % "1.12.2" % Test
)

fork in Test := true

