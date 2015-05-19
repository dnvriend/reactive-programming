name := "reactive-programming"

version := "1.0.0"

scalaVersion := "2.11.6"

resolvers += "dnvriend at bintray" at "http://dl.bintray.com/dnvriend/maven"

libraryDependencies ++= {
  val akkaVersion  = "2.3.11"
  Seq(
    "com.typesafe.akka"      %% "akka-actor"                     % akkaVersion,
    "com.typesafe.akka"      %% "akka-persistence-experimental"  % akkaVersion,
    "com.github.dnvriend"    %% "akka-persistence-inmemory"      % "1.0.1",
    "com.ning"               %  "async-http-client"              % "1.7.19",
    "org.jsoup"              %  "jsoup"                          % "1.8.1",
    "io.reactivex"           %% "rxscala"                        % "0.24.1",
    "org.scala-lang.modules" %% "scala-async"                    % "0.9.2",
    "com.typesafe.akka"      %% "akka-testkit"                   % akkaVersion % Test,
    "org.scalatest"          %% "scalatest"                      % "2.2.4"     % Test,
    "org.scalacheck"         %% "scalacheck"                     % "1.12.2"    % Test
  )
}

fork in Test := true

parallelExecution in Test := false
