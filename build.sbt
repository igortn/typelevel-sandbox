name := "typelevel-sandbox"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-Ypartial-unification"
)

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-core" % "1.0.4"
)

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")