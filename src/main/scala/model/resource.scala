package scalawave.model

case class ResourceId(id: String) extends AnyVal

case class Resource (
  uid: ResourceId,
  location: Location,
  skills: Option[Seq[SkillTag]],
  accountBlacklist: Option[Seq[AccountId]]
) extends HasUID[ResourceId]