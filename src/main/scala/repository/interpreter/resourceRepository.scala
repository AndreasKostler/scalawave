package scalawave.repository.interpreter

import cats.Functor
import cats.syntax.all._
import scalawave.db.algebra.KVS
import scalawave.model.{Resource, ResourceId, SkillTag}
import scalawave.repository.ResourceRepository

abstract class ResourceRepoKVInterp[F[_] : Functor]
  extends RepositoryKVInterpr[ResourceId, Resource, F] with ResourceRepository[F] {
  def withSkills(skill: SkillTag): F[Iterable[Resource]] = ???
}

object ResourceRepoKVInterp {
  def apply[F[_] : Functor](kvs: KVS[ResourceId, Resource, F]): ResourceRepoKVInterp[F] =
    ResourceRepoKVInterpImp(kvs)
}

case class ResourceRepoKVInterpImp[F[_]: Functor](kvs: KVS[ResourceId, Resource, F]) extends ResourceRepoKVInterp[F]