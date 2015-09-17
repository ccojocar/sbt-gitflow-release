package org.cosmin

import org.cosmin.GitflowRelease._
import sbt.{Plugin, Setting}
import sbtrelease.ReleaseStep
import sbtrelease.ReleaseStateTransformations._

object GitflowReleasePlugin extends Plugin {
  import sbtrelease.ReleasePlugin._

  private lazy val gitflowReleaseProcess =  ReleaseKeys.releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    pullChanges,
    checkGitFlowExists,
    gitFlowInit,
    inquireVersions,
    runClean,
    runTest,
    gitFlowReleaseStart,
    setReleaseVersion,
    commitReleaseVersion,
    publishArtifacts,
    gitFlowReleaseFinish,
    setNextVersion,
    commitNextVersion,
    pushDevelop,
    pushMaster,
    pushTags
  )

  lazy val gitflowReleaseSettings: Seq[Setting[_]] = releaseSettings ++ gitflowReleaseProcess
}
