package scalawave.repository.interpreter

import scalawave.db.algebra.KVS
import scalawave.model._
import cats.Functor
import cats.syntax.all._

abstract class JobRepoKVInterp[F[_]: Functor] extends RepositoryKVInterpr[JobId, Job, F] {
  def forAccount(aId: AccountId): F[Seq[Job]] =
    kvs.values.map(_.filter(job => job.accountId == aId))

  def forSkills(skill: SkillTag): F[Seq[Job]] =
    kvs.values.map(_.filter(job => job.skills.contains(skill)))
}

object JobRepoKVInterp {
  def apply[F[_] : Functor](kvs: KVS[JobId, Job, F]): JobRepoKVInterp[F] = JobRepoKVInterpImp(kvs)
}

case class JobRepoKVInterpImp[F[_]: Functor](kvs: KVS[JobId, Job, F]) extends JobRepoKVInterp[F]