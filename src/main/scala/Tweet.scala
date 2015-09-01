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
        case Some(gl) => Some(gl.getLatitude())
        case None => None
      },
      Option(geoLocation) match {
        case Some(gl) => Some(gl.getLongitude())
        case None => None
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
