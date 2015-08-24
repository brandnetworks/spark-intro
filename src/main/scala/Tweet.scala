package software.geowa4.twitter_consumer

object TwitterStatus {
  implicit def twitter4jStatusToTwitterStatus(status: twitter4j.Status): TwitterStatus = {
    val user = status.getUser()
    val geoLocation = status.getGeoLocation()
    if (geoLocation == null) {
      TwitterStatus(user.getId(), status.getText(), None, None)
    }
    else {
      TwitterStatus(
        user.getId(),
        status.getText(),
        Option(geoLocation.getLatitude()),
        Option(geoLocation.getLongitude())
      )
    }
  }
}

case class TwitterStatus(
  userId: Long,
  text: String,
  latitude: Option[Double],
  longitude: Option[Double]
)
