package scalawave.lesson1

import cats.Monad
import cats.free.Free
import cats.free.Free.liftF
import cats.~>

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

  class UpdateAux[V] {
    def apply[K](key: K, f: V => V): KVStore[K, V, Unit] =
      for {
        vMaybe <- get[K, V](key)
        _ <- vMaybe.map(v => put(key, f(v))).getOrElse(Free.pure(()))
      } yield ()
  }

  def update[V] = new UpdateAux[V]

  def values[K, V] = liftF[KVStoreA[K, V, ?], Iterable[V]](KVStoreA.Values[K, V]())

}

object FreeInterpreter {
  import FreeKVS._
  import cats.data.State

  private def interpreter[K, V] = new (KVStoreA[K, V, ?] ~> State[Map[K, V], ?]) {
    type S = Map[K, V]

    def apply[A](fa: KVStoreA[K, V, A]): State[S, A] = fa match {
      case KVStoreA.Put(k, v) => ???
      case KVStoreA.Get(k) => ???
      case KVStoreA.Values() => ???
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

// Lesson 3
object TTFIProgram {
  def program[F[_] : Monad](kvs: KVS[String, Int, F]): F[Option[Int]] = ???
}

import org.scalatest._

class InitiallyFinalSpec extends FlatSpec with Matchers {
  import FreeKVS._
  import scalawave.db.interpreter.PureKVSInterpreter

  "A program" should "yield the same results irrespective of embedding" in {
    FreeInterpreter.run(FreeProgram.program)(Map()) should
      be(TTFIProgram.program(PureKVSInterpreter.interpreter).run(Map()).value)
  }
  "A initial interpreter" should "be derived from a final interpreter" in {

    // Lesson 3
    def finalise[K, V, F[_]](to: KVS[K, V, F]): (KVStoreA[K, V, ?] ~> F) = ???

    val finalFromInitial = finalise(PureKVSInterpreter.interpreter[String, Int])
    FreeProgram.program.foldMap(finalFromInitial).run(Map()).value should
      be(FreeInterpreter.run(FreeProgram.program)(Map()))
  }
  "A final interpreter" should "be derived from an initial interpreter" in {

    // Lesson 3
    def initialise[K, V]: KVS[K, V, KVStore[K, V, ?]] = ???

    FreeInterpreter.run(TTFIProgram.program(initialise))(Map()) should
      be(TTFIProgram.program(PureKVSInterpreter.interpreter).run(Map()).value)
  }

}