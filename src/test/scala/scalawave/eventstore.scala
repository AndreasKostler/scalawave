package scalawave.repository

import org.scalatest._

import cats.Monad
import cats.implicits._

import monix.cats._
import monix.execution.Scheduler.Implicits.global

import scalawave.eventstore._
import scalawave.eventstore.interpreter.SeqEventStore

class EventstoreSpec extends FlatSpec with Matchers {
  "An event store" should "store and retrieve respective objects" in {
    def prog[F[_] : Monad](es: EventStore[F]) = for {
      _ <- es.append("foo", Record("a", 0))
      _ <- es.append("foo", Record("b", 1))
      _ <- es.append("foo", Record("c", 2))
      s <- es.subscribe[String, Int]("foo", None, Some(0))
      h <- es.latest[String, Int]("foo", None)
    } yield (s, h)

    prog(SeqEventStore()).run(Seq()).runAsync.map(_._2).value.get.get._2 should be(Some(Record("c", 2)))
  }
}