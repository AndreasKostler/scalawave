import scala.util.Success
import scalawave.db.algebra.KVS

import org.scalatest._
import cats.Monad
import cats.data.{Kleisli, ReaderT, State}
import cats.implicits._
import scalawave.repository._
import scalawave.model._


class InitiallyFinalSpec extends FlatSpec with Matchers {

  "A repository" should "store and retrieve it's respective objects" in {

    import scalawave.repository.interpreter._
    import scalawave.db.interpreter.PureKVSInterpreter

    def store[K, V <: HasUID[K], F[_]](repo: Repository[K, V, F]): ReaderT[F, V, Unit] = ???

    def retrieve[K, V <: HasUID[K], F[_]](repo: Repository[K, V, F]): ReaderT[F, V, Option[V]] = ???

    val resourceRepo: ResourceRepoKVInterp[State[Map[JobId, Job], ?]]  = ???
    val accountRepo: AccountRepoKVInterp[State[Map[JobId, Job], ?]] = ???
    val jobRepo: JobRepoKVInterp[State[Map[JobId, Job], ?]] = ???


    def storeAndRetrieve[K, V <: HasUID[K], F[_] : Monad](repo: Repository[K, V, F]): Kleisli[F, V, Option[V]] = ???

    val location = Location(Longitude(42.0), Latitude(42.0))

    val job = Job(
      JobId("job1"),
      AccountId("account1"),
      location,
      None
    )

    val resource = Resource(
      ResourceId("res1"),
      location,
      None,
      None
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