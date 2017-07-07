package scalawave.service

import scalawave.model._

object DummyData {
  val geocoding = Map(
    (Address(
      "34, Abingdon Street, Dutton Park, Brisbane, Brisbane City, Queensland, 4102, Australia"
    ) -> Location(Latitude(-27.49120965), Longitude(153.030475739125))),
    (Address(
      "McLachlan Street, Merthyr, Fortitude Valley, Brisbane, Brisbane City, Queensland, 4102, Australia"
    ) -> Location(Latitude(-27.4606185), Longitude(153.0350769))),
    (Address(
      "Powerhouse Park, Lamington Street, Merthyr, New Farm, Brisbane, Brisbane City, Queensland, 4102, Australia"
    ) -> Location(Latitude(-27.46783255), Longitude(153.054181957436)))
  )
  val reverseGeocoding = geocoding.map { case (k, v) => (v, k) }
}

trait LocationService[F[_]] {
  def geocode(address: Address): F[Location]

  def reverseGeocode(location: Location): F[Address]
}