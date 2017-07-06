# Prerequisites
## Scala and SBT
Please make sure you have a recent version of [SBT](http://www.scala-sbt.org/download.html) and [Scala](https://www.scala-lang.org/download/) installed
## Docker and Kafka
Install [Docker](https://docs.docker.com/docker-for-mac/install/#download-docker-for-mac) and [Kafka](https://hub.docker.com/r/wurstmeister/kafka/)

## Verify installation
TODO
## Clone repo
`git clone ...`
## Checkout base branch
`git checkout base`
# Peeling the Onion - Microservices One Layer at a Time

## Lession 1 - Familiarise yourself with the code base
Have a look around the code base. There is are `eventstore`, `model`, `service', and `repository` patterns which contain the domain 
models and algebras and interpreter for different parts of the microsevice. It will be your job implementing most of them :)
The unit tests should also give you a good indication of what we're trying to achieve in this workshop.

## Lession 2  - Warm up
Prove that the initial (deep) embedding is indeed dual to the final (shallow) encoding.
Head to `src/test/scala/scalawave/initiallyFinal.scala`
You will find a Key-Value store implementation based on `cats.Free`.
In order to make the unit tests pass you will need to:
1) Implement the `KVS` type-class: The TTFI representation of the above Key-Value store.
2) Write a program producing the same result as the given `Free` based program
3) Write a pure interpreter in the vain of `FreeInterpreter.interpreter`; you will find a skeleton implementation in 
`src/main/scala/interpreter/kvs.scala`
4) Write a `finalising` and `initialising` interpreter

## Lession 3 - Repositories
In the previous lesson we've developed a Key-Value store and a pure in-memory interpreter for it.
Now we can implement the repositories in `src/main/scala/repository/interpreter` in terms of 
our rock-solid `KVS` algebra. Go ahead and do just that. You might need to add some new combinators.

TODO: Write unit tests

## Lesson 4
Study the `JobService`, `ResourceService`, and `GeolocationService` algebras. 
Using these algebras, make the unit tests in `shared/test/scala/scalawave/assignJobs.scala` pass.
You will need to write a function

```scala
def assignJob(job: Job): F[Assignment]
```

Ensure the following business rules hold:
1) Resources need a valid location
2) Only resources that are NOT blacklisted for a certain account can perform jobs for those accounts
3) Only resources with all the skills required for a job can perform that job
4) The closest resource suitable for the job should be assigned

You can use the repositories to query for the required objects.

## Lesson 5
Write an interpreter for `EventStore` using an in memory data structure. Keep it simple, `List` et.al. will do.

## Lesson 6
The previous lessons taught us how to:
1) Write programs using our TTFI algebras
2) Combine different algebras in our programs
3) Combine programs to form more complex programs
4) Write interpreters for our algebras
5) Compose interpreters horizontally: Write interpreters in terms of interpreters 

In this lesson we will be looking at composing interpreters vertically: Write an interpreter for the 
`ResourceRepository` that 
Create geocoding service (Applicative)
`sbt test:test-only Lession3`
## Lesson 7
Event sourcings:
CreateAccount Command -> AccountCreated Event -> KVStore
`sbt test:test-only Lession4`
## Lesson 8
Event sourcing:
Kafka backend
`sbt test:test-only Lession5`
## Lesson 9
Read all at startup
Snapshots

## Taking things further:
1) Lesson 4: `monix-kafka` provides a reactive interface to Kafka. Rewrite the in-memory interpreter for `EventStore` 
using `monix-kafka`

2) CQRS - For the most part we've ignored the read part of an event sourced Microservice architecture. 
Write a service that listens to the published events and stores a relational model in a DB for easy querying. 

3) One drawback of TTFI compared to `Free` is the 'vertical' composability of interpreters. Can you come up with a way
of composing interpreters similarly to `Free`. E.g.
```scala
val horizontal = interp1 andThen interp2
val vertical = interp1 and interp2
```

4) Add a service gateweay using finch or http4s or your weapon of choice to expose the functionality of our service.

2) SOLID principle - Convince yourself that the TTFI pattern (and `Free` Monads) address every aspect of 
SOLID. 