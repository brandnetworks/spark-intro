Spider
======

Let's find bad people.

Getting Started
---------------

1. Install Ansible, Vagrant, Virtualbox, Scala (2.10), Apache Spark, and SBT (0.13).
1. Get AWS keys.
1. Optionally, export these variables if you intend to work with S3 in the Spark Shell.

        export AWS_ACCESS_KEY_ID=<your key id>
        export AWS_SECRET_ACCESS_KEY=<your secret access key>

1. Run the following commands.

        $ spark-shell
        scala> val data = Array(1, 2, 3, 4, 5)
        scala> val distData = sc.parallelize(data)
        scala> distData.reduce((a, b) => a + b)

For Spark Streaming with Twitter, make the necessary changes in Demo.scala and run this in the shell.

    $ sbt clean assembly
    $ spark-submit --master "local[2]" target/scala-2.10/spider-twitter.jar consumerKey consumerSecret accessToken accessTokenSecret
