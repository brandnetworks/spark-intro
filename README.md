Twitter Consumer
================

Let's pull some tweets!

Getting Started
---------------

1. Install Virtualbox, Docker, Docker Machine (with a machine called dev), Scala (2.10), Apache Spark, and SBT (0.13).
1. Run the following commands to test your installation.

        $ spark-shell
        scala> val data = Array(1, 2, 3, 4, 5)
        scala> val distData = sc.parallelize(data)
        scala> distData.reduce((a, b) => a + b)

1. Get the database running.

        $ ./dbup.sh

1. Run the app.

        $ sbt clean assembly
        $ export SPARK_CLASSPATH=~/.ivy2/cache/org.postgresql/postgresql/jars/postgresql-9.4-1201-jdbc41.jar
        $ spark-submit --master "local[2]" target/scala-2.10/test-twitter.jar consumerKey consumerSecret accessToken accessTokenSecret jdbc:postgresql://$(docker-machine ip dev):5432/postgres test development
