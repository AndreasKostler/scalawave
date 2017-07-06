package scalawave.lesson1

import cats.Monad
import cats.free.Free
import cats.free.Free.liftF
import cats.~>

import org.scalatest._

import scalawave.db.algebra._

object FreeKVS {

  sealed trait KVStoreA[K, V, A]

  object KVStoreA {

    final case class Put[K, V](k: K, v: V) extends KVStoreA[K, V, Unit]

    final case class Get[K, V](k: K) extends KVStoreA[K, V, Option[V]]

    final case class Values[K, V]() extends KVStoreA[K, V, Iterable[V]]

  }

  type KVStore[K, V, A] = Free[KVStoreA[K, V, ?], A]

  def put[K, V](k: K, v: V): KVStore[K, V, Unit] =
    Free.liftF[KVStoreA[K, V, ?], Unit](KVStoreA.Put(k, v))

  def get[K, V](key: K) = liftF[KVStoreA[K, V, ?], Option[V]](KVStoreA.Get[K, V](key))

  def values[K, V] = liftF[KVStoreA[K, V, ?], Iterable[V]](KVStoreA.Values[K, V]())

  class UpdateAux[V] {
    def apply[K](key: K, f: V => V): KVStore[K, V, Unit] =
      for {
        vMaybe <- get[K, V](key)
        _ <- vMaybe.map(v => put(key, f(v))).getOrElse(Free.pure(()))
      } yield ()
  }

  def update[V] = new UpdateAux[V]

}

object FreeInterpreter {

  import FreeKVS._
  import cats.data.State

  private def interpreter[K, V] = new (KVStoreA[K, V, ?] ~> State[Map[K, V], ?]) {
    type S = Map[K, V]

    def apply[A](fa: KVStoreA[K, V, A]): State[S, A] = fa match {
      case KVStoreA.Put(k, v) => State.modify(_ + (k -> v))
      case KVStoreA.Get(k) => State.inspect(s => s.get(k))
      case KVStoreA.Values() => State.inspect(_.values)
    }
  }

  def run[K, V, A](prog: Free[KVStoreA[K, V, ?], A])(st: Map[K, V]): (Map[K, V], A) =
    prog.foldMap(interpreter).run(st).value

}

object FreeProgram {

  import FreeKVS._

  def program: KVStore[String, Int, Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", (_ + 12))
      _ <- put("tame-cats", 5)
      n <- get("wild-cats")
    } yield n
}

// Lesson 2
object TTFIProgram {

  import scalawave.db.algebra._
  import cats.implicits._

  def program[F[_] : Monad](kvs: KVS[String, Int, F]): F[Option[Int]] =
    for {
      _ <- kvs.put("wild-cats", 2)
      _ <- kvs.update("wild-cats", (_ + 12))
      _ <- kvs.put("tame-cats", 5)
      n <- kvs.get("wild-cats")
    } yield n
}

class InitiallyFinalSpec extends FlatSpec with Matchers {

  import FreeKVS._
  import scalawave.db.interpreter.PureKVSInterpreter

  "A program" should "yield the same results irrespective of embedding" in {
    FreeInterpreter.run(FreeProgram.program)(Map()) should
      be(TTFIProgram.program(PureKVSInterpreter.interpreter).run(Map()).value)
  }
  "A initial interpreter" should "be derived from a final interpreter" in {

    // Lesson 2
    def finalise[K, V, F[_]](to: KVS[K, V, F]) = {
      new (KVStoreA[K, V, ?] ~> F) {
        def apply[A](kvs: KVStoreA[K, V, A]): F[A] = kvs match {
          case KVStoreA.Put(k, v) => to.put(k, v)
          case KVStoreA.Get(k) => to.get(k)
          case KVStoreA.Values() => to.values
        }
      }
    }

    val finalFromInitial = finalise(PureKVSInterpreter.interpreter[String, Int])
    FreeProgram.program.foldMap(finalFromInitial).run(Map()).value should
      be(FreeInterpreter.run(FreeProgram.program)(Map()))
  }
  "A final interpreter" should "be derived from an initial interpreter" in {

    // Lesson 2
    def initialise[K, V] = new KVS[K, V, KVStore[K, V, ?]] {
      def put(key: K, value: V): KVStore[K, V, Unit] = FreeKVS.put[K, V](key, value)

      def get(key: K): KVStore[K, V, Option[V]] = FreeKVS.get[K, V](key)

      def values: KVStore[K, V, Iterable[V]] = FreeKVS.values[K, V]
    }

    FreeInterpreter.run(TTFIProgram.program(initialise))(Map()) should
      be(TTFIProgram.program(PureKVSInterpreter.interpreter).run(Map()).value)
  }

}