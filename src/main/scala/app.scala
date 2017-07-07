package scalawave

import cats.Applicative
import cats.syntax.all._
import cats.Monad

import scalawave.model._
import scalawave.repository.{JobRepository, ResourceRepository}
import scalawave.service.{JobService, ResourceService, AccountService, LocationService}

object Scalawave {
  import cats.implicits._

  def main(args: Array[String]): Unit = {

    // Lesson 5
    // Create jobs
    // Create accounts
    // Create resources
    // Create locations
    // assign job to nearest resource satisfying business rules

  }
}