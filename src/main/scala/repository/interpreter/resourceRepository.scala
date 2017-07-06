package scalawave.repository.interpreter

import cats.Functor
import cats.syntax.all._

import scalawave.db.algebra.KVS
import scalawave.model.{SkillTag, Resource, ResourceId }

abstract class ResourceRepoKVInterp[F[_] : Functor] extends RepositoryKVInterpr[ResourceId, Resource, F] {
  def withSkills(skill: SkillTag): F[Seq[Resource]] =
    kvs.values.map(_.filter(res => res.skills.contains(skill)))
}

object ResourceRepoKVInterp {
  def apply[F[_] : Functor](kvs: KVS[ResourceId, Resource, F]): ResourceRepoKVInterp[F] =
    ResourceRepoKVInterpImp(kvs)
}

case class ResourceRepoKVInterpImp[F[_]: Functor](kvs: KVS[ResourceId, Resource, F]) extends ResourceRepoKVInterp[F]