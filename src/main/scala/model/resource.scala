package scalawave.model

sealed trait Status
object Status {

  case object Available extends Status

  case object OnJob extends Status

}
case class ResourceId(id: String) extends AnyVal

case class Resource (
  uid: ResourceId,
  location: Location,
  status: Status = Status.Available,
  skills: Option[Seq[SkillTag]] = None,
  accountBlacklist: Option[Seq[AccountId]] = None
) extends HasUID[ResourceId]