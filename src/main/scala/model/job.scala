package scalawave.model

case class JobId(id: String) extends AnyVal

case class Job(
  uid: JobId,
  accountId: AccountId,
  location: Location,
  skills: Option[Seq[SkillTag]]
) extends HasUID[JobId]