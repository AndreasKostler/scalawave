package scalawave.model

case class AccountId(id: String) extends AnyVal

case class Account(uid: AccountId, name: String) extends HasUID[AccountId]