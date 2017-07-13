package scalawave.repository.interpreter

import scalawave.db.algebra.KVS
import scalawave.model.{Account, AccountId}
import scalawave.repository.AccountRepository

trait AccountRepoKVInterp[F[_]] extends RepositoryKVInterpr[AccountId, Account, F] with AccountRepository[F]

object AccountRepoKVInterp {
  def apply[F[_]](kvs: KVS[AccountId, Account, F]) = AccountRepoKVInterpImpl(kvs)
}

case class AccountRepoKVInterpImpl[F[_]](kvs: KVS[AccountId, Account, F])
  extends AccountRepoKVInterp[F]