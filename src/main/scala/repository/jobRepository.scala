package scalawave.repository

import scalawave.model._

trait JobRepository[F[_]] extends Repository[JobId, Job, F] {
  def forAccount(aId: AccountId): F[Seq[Job]]

  def forSkills(s: SkillTag): F[Seq[Job]]
}