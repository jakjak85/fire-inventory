name := """fire-inventory"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.1.1"
libraryDependencies += "org.mongodb" % "bson" % "3.1.1"
libraryDependencies += "org.projectlombok" % "lombok" % "1.16.6"
libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

fork in run := true