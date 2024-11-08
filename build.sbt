ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "hotel-booking",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.18",
      "com.typesafe.akka" %% "akka-http" % "10.2.7",
      "com.typesafe.akka" %% "akka-stream" % "2.6.18",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.7",  
      "com.typesafe.slick" %% "slick" % "3.3.3",
      "com.h2database" % "h2" % "1.4.200",
      "ch.megard" %% "akka-http-cors" % "1.1.3",
    )
  )

Compile / mainClass := Some("Main")
