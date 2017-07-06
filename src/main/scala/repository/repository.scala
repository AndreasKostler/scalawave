package scalawave.repository

trait Repository[ID, V, F[_]] {
  def query(id: ID): F[Option[V]]

  def store(a: V): F[Unit]
}

object Repository {
  def apply[ID, V, F[_]](implicit repo: Repository[ID, V, F]): Repository[ID, V, F] = repo

  def query[ID, V, F[_]](id: ID)(implicit repo: Repository[ID, V, F]) = repo.query(id)

  def store[ID, V, F[_]](a: V)(implicit repo: Repository[ID, V, F]) = repo.store(a)
}