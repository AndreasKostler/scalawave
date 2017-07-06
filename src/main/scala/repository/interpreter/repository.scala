package scalawave.repository.interpreter

import scalawave.db.algebra.KVS
import scalawave.model.HasUID
import scalawave.repository.Repository

trait RepositoryKVInterpr[K, V <: HasUID[K], F[_]] extends Repository[K, V, F] {
  val kvs: KVS[K, V, F]

  def query(id: K): F[Option[V]] = kvs.get(id)

  def store(r: V): F[Unit] = kvs.put(r.uid, r)
}

object RepositoryKVInterpr {
  def apply[K, V <: HasUID[K], F[_]](kvs: KVS[K, V, F]): RepositoryKVInterpr[K, V, F] = RepositoryKVInterprImpl(kvs)
}

case class RepositoryKVInterprImpl[K, V <: HasUID[K], F[_]](kvs: KVS[K, V, F]) extends RepositoryKVInterpr[K, V, F]
