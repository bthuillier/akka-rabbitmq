name := "akka-stream-scala"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M2" ,
  "com.rabbitmq" % "amqp-client" % "3.4.3"
)

