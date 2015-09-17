## sbt-gitflow-release

An extension to the [sbt-release](https://github.com/sbt/sbt-release) plug-in to make it more compatible with [git flow](https://github.com/nvie/gitflow) and Artifactory repository.

## Introduction 

The steps from the build process are the following:

*  _checkSnapshotDependencies_ - checks dependencies to snapshot versions
*  _checkGitFlowExists_ - checks gitflow installation
*  _gitFlowInit_ - initializes the gitflow
*  _inquireVersions_ - prepares the release version 
*  _runClean_ - cleans up the project
*  _runTest_ - runs the unit tests 
*  _gitFlowReleaseStart_ - starts the git flow release 
*  _setReleaseVersion_ - updates the release version in the build.sbt
*  _commitReleaseVersion_ - commits the changes in local git repo
*  _publishArtifacts_ - publishes the artifacts into Artifactory
*  _gitFlowReleaseFinish_ - finishes the git flow release by merging the release branch in both develop and master
*  _setNextVersion_ - updates the development version in the build.sbt
*  _commitNextVersion_ - commits the changes in the local git repo
*  _pushDevelop_ - pushes to upstream the local develop branch  
*  _pushMaster_ - pushes to upstream the local master branch
*  _pushTags_ - pushes to upstream the tags

     
## Requirements
 
 Latest version of *gitflow*
 
    git clone --recursive git://github.com/nvie/gitflow.git
    cd gitflow
    sudo make install
    git config --global core.mergeoptions --no-edit

## Installation 

### Local

    sbt ^publishLocal

### Remote

    sbt ^publish

## Usage

For anything other than the basic instructions provided here, refer to the [sbt-release](https://github.com/sbt/sbt-release) documentation.

### Adding the plug-in dependency to your project

Add the following lines to `./project/plugins.sbt`. 

    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
    
    resolvers += Resolver.url("Artifactory", new URL("https://repo.jfrog.org/artifactory/releases/"))(Resolver.ivyStylePatterns)

    addSbtPlugin("org.cosmin" % "sbt-gitflow-release" % "1.0.0", "0.13.5")

The sbt version must be the same like the `sbt.version` from `./project/build.properties`. 

### Including sbt-gitflow-release settings in your build
**Important:** The settings `gitflowReleaseSettings` need to be mixed into every sub-projects `settings`.
This is usually achieved by extracting common settings into a `val standardSettings: Seq[Setting[_]]` which is then included in all sub-projects.

#### build.sbt (simple build definition)

    import org.cosmin.GitflowReleasePlugin._
    
    gitflowReleaseSettings

#### Build.scala (full build definition)

    import org.cosmin.GitflowReleasePlugin._

    object MyBuild extends Build {
      lazy val MyProject(
        id = "myproject",
        base = file("."),
        settings = Defaults.defaultSettings ++ gitflowReleaseSettings ++ Seq( /* custom settings here */ )
      )
    }

### Starting the release process

**Important:** The names of your git branches must follow the naming convention defined in gitflow:

*  `master` - release branch
*  `develop` - development branch

 
    git checkout develop
    sbt 'release with-defaults'
