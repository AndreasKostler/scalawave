package scalawave.eventstore.interpreter

import cats.{MonadState, ~>}
import cats.data.StateT

import monix.eval.Task
import monix.reactive.Observable
import monix.cats._

import scalawave.eventstore._

trait SeqEventStore {
  type F[x] = StateT[Task, Seq[Record[_, _]], x]
  val stateMonad = MonadState[F, Seq[Record[_, _]]]

  def apply() = new EventStore[F] {

    def subscribe[K, V](topic: String, key: Option[K], offset: Option[Long]): F[Observable[Record[K, V]]] = {
      val maybeDrop = (l: Seq[Record[K, V]]) => offset.map(o => l.drop(o.toInt)).getOrElse(l)
      val maybeFilter = (l: Seq[Record[K, V]]) => key.map(k => l.filter(rec => rec.key == k)).getOrElse(l)

      val toObservable =
        maybeDrop andThen maybeFilter andThen Observable.fromIterable[Record[K, V]]

      stateMonad.get.map(rs => rs.map(r => Record(r.asInstanceOf[K], r.asInstanceOf[V]))).map(toObservable)
    }

    def append[K, V](topic: String, record: Record[K, V]): F[Either[EventStoreError, EventMetadata]] =
      stateMonad
        .get
        .modify(l => record +: l)
        .map(l => Right(EventMetadata(l.length - 1L)): Either[EventStoreError, EventMetadata])

    def latest[K, V](topic: String, key: Option[K]): F[Option[Record[K, V]]] =
      stateMonad.get.map(l => l.headOption.map(r => Record(r.key.asInstanceOf[K], r.value.asInstanceOf[V])))
  }
}

object SeqEventStore extends SeqEventStore