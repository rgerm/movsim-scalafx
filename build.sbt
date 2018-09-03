import sbt.Resolver

name := "movscalafx"

organization := "de.movsim"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.1"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.scalafx"   %% "scalafx"   % "8.0.102-R11",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.movsim" % "MovsimCore" % "1.7.0-SNAPSHOT",
  "org.jfree" % "fxgraphics2d" % "1.7"
)

shellPrompt := { state => System.getProperty("user.name") + "> " }

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
mainClass in (Compile, run) := Some("de.movsim.scalafx.Viewer")

// Fork a new JVM for 'run' and 'test:run' to avoid JavaFX double initialization problems
fork := true