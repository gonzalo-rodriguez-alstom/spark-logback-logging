
organization in ThisBuild := "com.example"

scalaVersion in ThisBuild := "2.11.8"
resolvers += "confluent.io" at "http://packages.confluent.io/maven/"

updateOptions := updateOptions.value.withGigahorse(false)
// Setting javac options in common allows IntelliJ IDEA to import them automatically
javacOptions in compile ++= Seq(
  "-encoding", "UTF-8",
  "-source", "1.8",
  "-target", "1.8",
  "-parameters",
  "-Xlint:unchecked",
  "-Xlint:deprecation"
)

name in (ThisBuild, Compile, assembly) := "test-project"

assemblyJarName in assembly := s"${name.value}_${scalaVersion.value}-${version.value}-assembly.jar"

// Skip the tests (comment out to run the tests).
test in assembly := {}

// Publish Fat JARs. Libraries should not publish fat jars.
artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

val sparkVersion ="2.3.0"
val logbackVersion = "1.2.3"
val scalaLoggingVersion = "3.9.0"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  , "net.logstash.logback" % "logstash-logback-encoder" %"6.1"
  , "ch.qos.logback" % "logback-classic" % logbackVersion
  , "ch.qos.logback" % "logback-classic" % logbackVersion
  , "org.slf4j" % "log4j-over-slf4j" % "1.7.25"

  , "org.apache.spark" %% "spark-core" % sparkVersion % "provided" excludeAll( ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "log4j"))
  , "org.apache.spark" %% "spark-sql" % sparkVersion  % "provided" excludeAll( ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "log4j"))
  , "org.apache.spark" %% "spark-mllib" % sparkVersion  % "provided" excludeAll( ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "log4j"))
  , "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion  excludeAll( ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "log4j"))
  , "org.apache.spark" %% "spark-hive" % "2.4.0"  % "provided" excludeAll( ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "log4j"))



)


assemblyMergeStrategy in assembly := {

  case PathList("com",   "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com",   "squareup", xs @ _*) => MergeStrategy.last
  case PathList("com",   "sun", xs @ _*) => MergeStrategy.last
  case PathList("com",   "thoughtworks", xs @ _*) => MergeStrategy.last
  case PathList("commons-beanutils", xs @ _*) => MergeStrategy.last
  case PathList("commons-cli", xs @ _*) => MergeStrategy.last
  case PathList("commons-collections", xs @ _*) => MergeStrategy.last
  case PathList("commons-io", xs @ _*) => MergeStrategy.last
  case PathList("io",    "netty", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "xml", xs @ _*) => MergeStrategy.last
  case PathList("org",   "apache", xs @ _*) => MergeStrategy.last
  case PathList("org",   "codehaus", xs @ _*) => MergeStrategy.last
  case PathList("org",   "glassfish", xs @ _*) => MergeStrategy.last
  case PathList("org",   "fusesource", xs @ _*) => MergeStrategy.last
  case PathList("org",   "mortbay", xs @ _*) => MergeStrategy.last
  case PathList("org",   "tukaani", xs @ _*) => MergeStrategy.last
  case PathList("com",   "twitter", xs @ _*) => MergeStrategy.last
  case PathList("org",   "objenesis", xs @ _*) => MergeStrategy.last
  case PathList("org", "aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("xerces", xs @ _*) => MergeStrategy.last
  case PathList("xmlenc", xs @ _*) => MergeStrategy.last
  case PathList("net.jpountz", xs @ _*) => MergeStrategy.last

  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "git.properties" => MergeStrategy.last
  case "plugin.xml" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "parquet.thrift" => MergeStrategy.last
  case "codegen/config.fmpp" => MergeStrategy.last


  // Needed only to sbt assembly non provided spark-streaming-kafka-0-10
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
  // Needed only to sbt assembly etcd coming with libraries
  case PathList("META-INF", "io.netty.versions.properties", xs @ _*) => MergeStrategy.last
  case PathList("scala","collection","mutable", xs @ _*) => MergeStrategy.first
  case PathList("scala","util", xs @ _*) => MergeStrategy.first
  case PathList("library.properties", xs @ _*) => MergeStrategy.first
  case PathList("logback.xml",xs @ _ *) => MergeStrategy.last
  case x => (assemblyMergeStrategy in assembly).value(x)
  //case PathList("MANIFEST.MF",xs @ _ *) => MergeStrategy.first
  // case _ => MergeStrategy.first
}

logLevel in assembly := Level.Info


// The default SBT testing java options are too small to support running many of the tests due to the need to launch Spark in local mode.
parallelExecution in Test := false
fork in Test := true
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

updateOptions := updateOptions.value.withLatestSnapshots(false)



