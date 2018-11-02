organization in ThisBuild := "datahack.bootcamp"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

//Desabilitamos kafka
lagomKafkaEnabled in ThisBuild := false

lazy val `introlagom` = (project in file("."))
  .aggregate(`mensajes-api`, `mensajes-impl`, `mensajes-stream-api`, `mensajes-stream-impl`)

lazy val `mensajes-api` = (project in file("mensajes-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `mensajes-impl` = (project in file("mensajes-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`mensajes-api`)

lazy val `mensajes-stream-api` = (project in file("mensajes-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `mensajes-stream-impl` = (project in file("mensajes-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`mensajes-stream-api`, `mensajes-api`)
