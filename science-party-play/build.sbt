name := """science-party-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

// Test Libs
//libraryDependencies  += "org.seleniumhq.selenium" % "selenium -firefox -driver" % "2.52.0" % "test"
//libraryDependencies  += "org.seleniumhq.selenium" % "selenium -java" % "2.52.0" % "test"
libraryDependencies  += "junit" % "junit" % "4.12" % "test"

// MySQL Connector
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

//Database Evolution
libraryDependencies += evolutions

// Ebean ORM
lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true