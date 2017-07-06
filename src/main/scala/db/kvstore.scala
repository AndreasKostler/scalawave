package scalawave.db.algebra

import cats.Monad
import cats.implicits._

trait KVS[K, V, F[_]] {

  def put(key: K, value: V): F[Unit]

  def get(key: K): F[Option[V]]

  def values: F[Seq[V]]
}

object KVS {

  def apply[K, V, F[_]](implicit F: KVS[K, V, F]): KVS[K, V, F] = F

  implicit class Syntax[K, V, F[_]](kvs: KVS[K, V, F]) {

    def update(key: K, f: V => V)(implicit M: Monad[F]) = for {
      vMaybe <- kvs.get(key)
      _ <- vMaybe.map(v => kvs.put(key, f(v))).getOrElse(M.pure(()))
    } yield ()
  }

  def get[K, V, F[_]](key: K)(implicit kvs: KVS[K, V, F]) = kvs.get(key)

  def values[K, V, F[_]](implicit kvs: KVS[K, V, F]) = kvs.values

}


