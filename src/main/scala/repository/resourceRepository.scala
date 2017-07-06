package scalawave.repository

import scalawave.model.{Resource, ResourceId, SkillTag}

trait ResourceRepository[F[_]] extends Repository[ResourceId, Resource, F] {
  def withSkills(s: SkillTag): F[Iterable[Resource]]
}