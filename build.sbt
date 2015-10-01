import sbt._
import Defaults._

name := "sbt-gitflow-release"

organization := "org.cosmin"

version := "1.0.0"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-feature",  "-language:postfixOps")

sbtPlugin := true

libraryDependencies += sbtPluginExtra(
  m = "com.github.gseitz" % "sbt-release" % "0.8.5",
  sbtV = "0.13",
  scalaV = "2.10"
)

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.13", "0.13.1", "0.13.2", "0.13.5", "0.13.6", "0.13.7", "0.13.8")

CrossBuilding.scriptedSettings

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

bintrayVcsUrl := Some("git@github.com:cosmin-cojocar/sbt-gitflow-release.git")

val publishSettings = Seq(
  publishMavenStyle := false,
  publishTo <<= (version) { version: String =>
    val repo = "http://oss.jfrog.org/artifactory/"
    val (name, url) = if (version.contains("-SNAPSHOT"))
      ("snapshots", repo + "oss-snapshot-local")
    else
      ("releases", repo + "oss-release-local")
    Some(Resolver.url(name, new URL(url))(Resolver.mavenStylePatterns))
  },
  bintrayReleaseOnPublish := false,
  credentials := List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
)