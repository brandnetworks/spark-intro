package software.geowa4.twitter_consumer

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.log4j.Logger
import org.apache.log4j.Level
import TwitterStatus._

object TwitterConsumer {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val conf = new SparkConf().setAppName("TwitterConsumer")
    val ssc = new StreamingContext(conf, Seconds(5))
 
    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generat OAuth credentials
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
    val stream = TwitterUtils.createStream(ssc, None)

    val numWithLocation = stream.map(_.latitude match {
      case Some(_) => 1
      case None => 0
    })
    .reduce(_ + _)
    numWithLocation.print()
    stream.count().print()

    val hashTags = stream.flatMap(status => status.text.split(" ").filter(_.startsWith("#")))

    val topCounts60 = hashTags
      .map((_, 1))
      .reduceByKeyAndWindow(_ + _, Seconds(60))
      .map(_.swap)
      .transform(_.sortByKey(false))

    val topCounts10 = hashTags
      .map((_, 1))
      .reduceByKeyAndWindow(_ + _, Seconds(10))
      .map(_.swap)
      .transform(_.sortByKey(false))

    // Print popular hashtags
    topCounts60.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
      topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
    })

    topCounts10.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
      topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
