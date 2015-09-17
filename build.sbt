import sbt._
import Defaults._

name := "sbt-gitflow-release"

organization := "org.cosmin"

version := "1.0.0"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-feature",  "-language:postfixOps")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

sbtPlugin := true

libraryDependencies += sbtPluginExtra(
  m = "com.github.gseitz" % "sbt-release" % "0.8.5",
  sbtV = "0.13",
  scalaV = "2.10"
)

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.13", "0.13.1", "0.13.2", "0.13.5", "0.13.6", "0.13.7", "0.13.8")

CrossBuilding.scriptedSettings

publishTo <<= (version) { version: String =>
  val repo = "https://repo.jfrog.org/artifactory"
  val (name, url) = if (version.contains("-SNAPSHOT"))
    ("snapshots", repo + "snapshots")
  else
    ("releases", repo + "releases")
  Some(Resolver.url(name, new URL(url))(Resolver.ivyStylePatterns))
}

publishMavenStyle := false
