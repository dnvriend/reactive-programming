name := "reactive-programming"

version := "1.0.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9" withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "2.2.4" % Test withSources() withJavadoc()
)

fork in Test := true

