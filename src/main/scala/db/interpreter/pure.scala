package scalawave.db.interpreter

import cats.data.State
import scalawave.db.algebra._

object PureKVSInterpreter {

  def interpreter[K, V]: KVS[K, V, State[Map[K, V], ?]] = ???
}