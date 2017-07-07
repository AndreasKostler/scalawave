package scalawave.service

import scalawave.model._

trait JobService[F[_]] {
  def create(id: JobId, accountId: AccountId, location: Location): F[Job]

  def addRequiredSkills(id: JobId, skills: SkillTag*): F[Job]
}