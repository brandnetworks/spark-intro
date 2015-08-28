package software.geowa4.twitter_consumer

object TwitterStatus {
  implicit def twitter4jStatusToTwitterStatus(status: twitter4j.Status): TwitterStatus = {
    val user = status.getUser()
    val geoLocation = status.getGeoLocation()
    TwitterStatus(
      status.getId(),
      user.getId(),
      status.getText(),
      Option(geoLocation) match {
        case None => None;
        case Some(gl) => Some(gl.getLatitude())
      },
      Option(geoLocation) match {
        case None => None;
        case Some(gl) => Some(gl.getLongitude())
      }
    )
  }
}

case class TwitterStatus(
  id: Long,
  userId: Long,
  text: String,
  latitude: Option[Double],
  longitude: Option[Double]
)
