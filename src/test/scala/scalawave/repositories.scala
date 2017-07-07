package scalawave.repository

import org.scalatest._

import cats.Monad
import cats.data.ReaderT

import scalawave.model._

class RepositoriesSpec extends FlatSpec with Matchers {
  "A repository" should "store and retrieve it's respective objects" in {

    import scalawave.repository.interpreter._
    import scalawave.db.interpreter.PureKVSInterpreter

    def store[K, V <: HasUID[K], F[_]](repo: Repository[K, V, F]): ReaderT[F, V, Unit] =
      ReaderT { value => repo.store(value) }

    def retrieve[K, V <: HasUID[K], F[_]](repo: Repository[K, V, F]): ReaderT[F, V, Option[V]] =
      ReaderT { value => repo.query(value.uid) }

    val resourceRepo = ResourceRepoKVInterp(PureKVSInterpreter.interpreter[ResourceId, Resource])
    val accountRepo = AccountRepoKVInterp(PureKVSInterpreter.interpreter[AccountId, Account])
    val jobRepo = JobRepoKVInterp(PureKVSInterpreter.interpreter[JobId, Job])

    def storeAndRetrieve[K, V <: HasUID[K], F[_] : Monad](repo: Repository[K, V, F]) = for {
      _ <- store(repo)
      v <- retrieve(repo)
    } yield v

    val location = Location(Latitude(42.0), Longitude(42.0))

    val job = Job(
      JobId("job1"),
      AccountId("account1"),
      location,
      None
    )

    val resource = Resource(
      ResourceId("res1"),
      location
    )

    val account = Account(AccountId("acc1"), "Scalac")

    val accountStorage = storeAndRetrieve(accountRepo)
    val jobStorage = storeAndRetrieve(jobRepo)
    val resourceStorage = storeAndRetrieve(resourceRepo)

    jobStorage(job).run(Map()).value._2 should be(Some(job))
    resourceStorage(resource).run(Map()).value._2 should be(Some(resource))
    accountStorage(account).run(Map()).value._2 should be(Some(account))
  }


  "A job repository" should "retrieve jobs for specific skills" in {
    (1) should be(2)
  }

  "A job repository" should "retrieve jobs for specific accounts" in {
    (1) should be(2)
  }

  "A resource repository" should "retrieve resource having specific skills" in {
    (1) should be(2)
  }

}