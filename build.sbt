name := "chgk_duel"

version := "1.0"

lazy val `chgk_duel` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "net.ruippeixotog" %% "scala-scraper" % "1.1.0",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.14" % "test",
  "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test",
  "org.mockito" % "mockito-core" % "2.2.25" % "test",
  "org.scalatestplus.play" % "scalatestplus-play_2.11" % "2.0.0-M1" % "test"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  
