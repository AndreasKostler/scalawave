package scalawave.service

import scalawave.model._

trait AccountService[F[_]] {
  def create(id: AccountId, name: String): F[Account]
}