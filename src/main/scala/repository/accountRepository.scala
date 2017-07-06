package scalawave.repository

import scalawave.model.{AccountId, Account}

trait AccountRepository[F[_]] extends Repository[AccountId, Account, F]