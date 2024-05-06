import rocks.muki.graphql.GraphQLCodegenPlugin.autoImport.{JsonCodec, graphqlCodegenJson}

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "graphql-demo",
    graphqlCodegenJson := JsonCodec.PlayJson
  )

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "4.1.0",
  "org.playframework" %% "play-json" % "3.0.3",
  "org.apache.pekko" %% "pekko-http" % "1.0.1",
  "org.apache.pekko" %% "pekko-actor" % "1.0.2",
  "org.apache.pekko" %% "pekko-stream" % "1.0.2"
)

enablePlugins(GraphQLSchemaPlugin, GraphQLQueryPlugin)
enablePlugins(GraphQLCodegenPlugin)

