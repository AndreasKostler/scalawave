package scalawave.db.interpreter

import cats.data.State
import scalawave.db.algebra._

object PureKVSInterpreter {

  def interpreter[K, V] = new KVS[K, V, State[Map[K, V], ?]] {
    type S = Map[K, V]

    def put(k: K, v: V): State[S, Unit] = ???

    def get(k: K): State[S, Option[V]] = ???

    def values: State[S, Iterable[V]] = ???
  }
}