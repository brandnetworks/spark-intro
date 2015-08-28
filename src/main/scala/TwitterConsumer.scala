package software.geowa4.twitter_consumer

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.sql._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import java.util.Properties
import org.apache.log4j.Logger
import org.apache.log4j.Level
import TwitterStatus._

object TwitterConsumer {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val conf = new SparkConf().setAppName("TwitterConsumer")
    val ssc = new StreamingContext(conf, Seconds(30))
 
    val Array(
      consumerKey,
      consumerSecret,
      accessToken,
      accessTokenSecret,
      jdbcUrl,
      jdbcUser,
      jdbcPassword
    ) = args.take(7)
    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generat OAuth credentials
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
    val stream = TwitterUtils.createStream(ssc, None)

    val streamWithLocation = stream
      .filter(_.latitude match {
        case Some(_) => true
        case None => false
      })
    streamWithLocation.count().print()
    stream.count().print()

    streamWithLocation
      .foreachRDD { rdd =>
        val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
        import sqlContext.implicits._

        val statuses = rdd
          .map(s => (s.id, s.userId, s.text, s.latitude, s.longitude))
          .toDF("id", "user_id", "text", "latitude", "longitude")
        val connectionProps = new Properties()
        connectionProps.setProperty("user", jdbcUser)
        connectionProps.setProperty("password", jdbcPassword)
        // checkpoint
        statuses.write.mode("append").jdbc(jdbcUrl, "twitter_statuses", connectionProps)
        // checkpoint
      }

    ssc.start()
    ssc.awaitTermination()
  }
}
