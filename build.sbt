name := "reactive-programming"

version := "1.0.0"

scalaVersion := "2.11.6"

libraryDependencies ++= {
  val akkaVersion = "2.3.10"
  Seq(
    "com.typesafe.akka"      %% "akka-actor"      % akkaVersion,
    "io.reactivex"           %% "rxscala"         % "0.24.1",
    "org.scala-lang.modules" %% "scala-async"     % "0.9.2",
    "com.typesafe.akka"      %% "akka-testkit"    % akkaVersion % Test,
    "org.scalatest"          %% "scalatest"       % "2.2.4"     % Test,
    "org.scalacheck"         %% "scalacheck"      % "1.12.2"    % Test
  )
}

fork in Test := true

