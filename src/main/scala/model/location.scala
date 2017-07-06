package scalawave.model

case class Longitude(lng: Double) extends AnyVal
case class Latitude(lat: Double) extends AnyVal

case class Location(lng: Longitude, lat: Latitude)
