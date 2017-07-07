package scalawave.service

import scalawave.model._

trait ResourceService[F[_]] {
  def create(id: ResourceId, location: Location): F[Resource]

  def blacklistAccounts(id: ResourceId, acts: AccountId*): F[Resource]

  def addSkills(id: ResourceId, skills: SkillTag*): F[Resource]

  def assignJob(rId: ResourceId, jId: JobId): F[Option[Assignment]]
}