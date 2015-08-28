name := "twitter-test"

version := "1.0"

scalaVersion := "2.10.5"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
}

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.4.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.4.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.4.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.4.1" 
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"

assemblyJarName in assembly := "test-twitter.jar"

seq(flywaySettings: _*)

flywayUrl := "jdbc:postgresql://192.168.99.100:5432/postgres"
flywayUser := "test"
flywayPassword := "development"
