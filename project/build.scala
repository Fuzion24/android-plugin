import sbt._
import Keys._

object Versions {
  val scala     = "2.10.2"
  val scalatest = "1.9.1"
}

object AndroidBuild extends Build {
	lazy val sbtAndroid = Project("sbt-android", file("."), settings = pluginSettings)

	lazy val pluginSettings = Defaults.defaultSettings ++ Seq(
		name := "sbt-android",
		organization := "org.scala-sbt",
		version := "0.7-SNAPSHOT",
		scalaVersion := Versions.scala,
		scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-Xfatal-warnings", "-feature", "-language:postfixOps"),
		publishMavenStyle := false,
		publishTo <<= (version) { version: String =>
		    val scalasbt = "http://scalasbt.artifactoryonline.com/scalasbt/"
		    val (name, url) = if (version.contains("-"))
		                        ("sbt-plugin-snapshots", scalasbt+"sbt-plugin-snapshots")
		                      else
		                        ("sbt-plugin-releases", scalasbt+"sbt-plugin-releases")
		    Some(Resolver.url(name, new URL(url))(Resolver.ivyStylePatterns))
		},
		credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
		libraryDependencies ++= Seq(
		  "com.google.android.tools" % "ddmlib" % "r10",
		  "net.sf.proguard" % "proguard-base" % "4.8"
		),
		sbtPlugin := true,
		commands += Status.stampVersion
	)
}