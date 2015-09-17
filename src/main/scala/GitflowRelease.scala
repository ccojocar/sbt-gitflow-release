package org.cosmin

import sbt._
import sbtrelease.ReleasePlugin.ReleaseKeys
import sbtrelease.ReleaseStep

object GitflowRelease {
  def execShell(s: String) = {
    List("sh", "-c", "(" + s + ")") ! new ProcessLogger {
      override def info(s: => String) { println(s) }
      override def error(s: => String) { println(s) }
      override def buffer[T](t: => T) = t
    }
  }

  def execStep(fn: State => String): ReleaseStep = { st: State =>
    val command = fn(st)
    st.log.info("Executing '%s':" format command)
    execShell(command)
    st
  }

  def releaseVersion(st: State): String =
    st.get(ReleaseKeys.versions).map { _._1 }.getOrElse(sys.error("No versions are! Was this release part executed before inquireVersions?"))

  lazy val checkGitFlowExists: ReleaseStep = { st: State =>
    val command = "command -v git-flow || echo"
    List("sh", "-c", "(" + command + ")") !! match {
      case "echo\n" => sys.error("git-flow is required for release. See https://github.com/nvie/gitflow for installation instructions.")
      case _ => st
    }
  }

  lazy val gitFlowInit = execStep { _ => "git flow init -fd" }

  lazy val gitFlowReleaseStart = execStep { "git flow release start " + releaseVersion(_) }

  lazy val gitFlowReleaseFinish = execStep { st =>
    val rv = releaseVersion(st)
    List(
      "echo 'Releasing %s.' > .git/MY_TAGMSG".format(rv),
      "git flow release finish -f .git/MY_TAGMSG " + rv
    ).mkString("; ")
  }

  lazy val pushMaster = execStep { _ =>
    List(
      "git checkout master",
      "git push origin master"
    ).mkString("; ")
  }

  lazy val pushDevelop = execStep { _ =>
    List(
      "git checkout develop",
      "git push origin develop"
    ).mkString("; ")
  }

  lazy val pushTags = execStep { _ => "git push origin --tags" }


  lazy val pullChanges = execStep { _ =>
    List(
      "git pull origin master",
      "git pull origin develop"
    ).mkString("; ")
  }
}