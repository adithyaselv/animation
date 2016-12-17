import com.lihaoyi.workbench.Plugin._
import com.lihaoyi.workbench.Plugin._
import org.scalajs.sbtplugin.ScalaJSPlugin

enablePlugins(ScalaJSPlugin)

workbenchSettings


name := "animation"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "org.scalaz" %% "scalaz-effect" % "7.2.0",
  "com.github.japgolly.fork.scalaz" %%% "scalaz-effect" % "7.2.0"
)

bootSnippet := "example.ScalaJSExample().main(document.getElementById('canvas'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
