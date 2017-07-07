package scalawave

import cats.Applicative
import cats.syntax.all._
import cats.Monad

import scalawave.model._
import scalawave.repository.{JobRepository, ResourceRepository}
import scalawave.service.{JobService, ResourceService, AccountService, LocationService}

object Scalawave {
  import cats.implicits._

  def main(args: Array[String]): Unit = {

    val jobAddresses = List(Address("Foo"), Address("Bar"))
    val jobIds = List(JobId("j1"), JobId("j2"))
    val resourceAddresses = List(Address("Baz"))
    val resourceIds = List(ResourceId("r1"))

    // Lesson 5
    // Create jobs
    def createJobs[F[_] : Monad](
      jobIds: List[JobId],
      acctIds: List[AccountId],
      locs: List[Location]
    )(implicit
      as: AccountService[F],
      ls: LocationService[F],
      js: JobService[F]
    ): F[List[Job]] = (jobIds, acctIds, locs).zipped.toList.traverseU {
      case (jId, aId, l) => (js.create(jId, aId, l))
    }

    // Create accounts
    def createAccounts[F[_] : Applicative](
      accIds: List[AccountId],
      accNames: List[String]
    )(implicit as: AccountService[F]): F[List[Account]] =
      (accIds zip accNames).traverseU { case (id, name) => as.create(id, name) }

    // Create resources

    def createResources[F[_] : Monad](resIds: List[ResourceId])(implicit
      ls: LocationService[F],
      rs: ResourceService[F]
    ): F[List[Resource]] = for {
      locs <- createLocations[F](resourceAddresses)
      resources <- (resourceIds zip locs).traverseU { case (id, l) => rs.create(id, l) }
    } yield resources

    // Create locations
    def createLocations[F[_] : Applicative](addrs: List[Address])(implicit ls: LocationService[F]): F[List[Location]] =
      addrs.traverseU(ls.geocode(_))

    // assign job to nearest resource satisfying business rules


  }
}