import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "fedakka"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.akka" %% "akka-zeromq" % "2.1.2",
    "com.typesafe.play.plugins" %% "play-statsd" % "2.1.0",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "mDialog releases" at "http://mdialog.github.io/releases/"
  )

}
