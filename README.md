# Prerequisites
## Scala and SBT
Please make sure you have a recent version of [SBT](http://www.scala-sbt.org/download.html) and [Scala](https://www.scala-lang.org/download/) installed
## Clone repo
`git clone https://github.com/AndreasKostler/scalawave.git`
## Checkout master branch
`git checkout master`
# Peeling the Onion - Microservices One Layer at a Time
The aim of this workshop is to get an appreciation of how `TTFI`, and interpreters in general, provide abstraction and
modularity for functional, effectful programs. In particular, they allow for a natural implementation of layered (Onion)
architectures by compiling to increasingly lower level languages beginning at the core of the business domain. 

Our business domain is the domain of job scheduling. 

## Lesson 1 - Familiarise yourself with the code base
Have a read of [this](https://softwaremill.com/free-tagless-compared-how-not-to-commit-to-monad-too-early/). Adam does
an awesome job of explaining the differences between `Free` and `TTFI` - better than I ever could.

Have a look around the code base. There is are `eventstore`, `model`, `service', and `repository` patterns which contain the domain 
models and algebras and interpreter for different parts of the microsevice. It will be your job implementing most of them :)
The unit tests should also give you a good indication of what we're trying to achieve in this workshop.

## Lesson 2  - Warm up
Prove that the initial (deep) embedding is indeed dual to the final (shallow) encoding.
Head to `src/test/scala/scalawave/initiallyFinal.scala`
You will find a Key-Value store implementation based on `cats.Free`.
In order to make the unit tests pass you will need to:
1) Implement the `KVS` type-class: The TTFI representation of the above Key-Value store.
2) Write a program producing the same result as the given `Free` based program
3) Write a pure interpreter in the vain of `FreeInterpreter.interpreter`; you will find a skeleton implementation in 
`src/main/scala/interpreter/kvs.scala`
4) Write a `finalising` and `initialising` interpreter

## Lesson 3 - Repositories
In the previous lesson we've developed a Key-Value store and a pure in-memory interpreter for it.
Now we can implement the repositories in `src/main/scala/repository/interpreter` in terms of 
our rock-solid `KVS` algebra. Go ahead and do just that. You might need to add some new combinators.

## Lesson 4 - Services
Study the `JobService`, `ResourceService`, and `LocationService` algebras. 
We have repositories available now that the services can interact with. There are mock maps for the 
`LocationService`. 

Using these algebras, make the unit tests in `src/test/scala/scalawave/services.scala` pass.

## Lesson 5 - The Main Program
Our main program will be quite simplistic (feel free to pad this out as much as you like). Given a job, 
assign that job to the closest, available resource.

Ensure the following business rules hold:

1) Resources and jobs need a valid location
2) Only resources that are NOT blacklisted for a certain account can perform jobs for those accounts
3) Only resources with all the skills required for a job can perform that job
4) The closest resource suitable for the job should be assigned
5) When a job gets assigned to a resource the status for the resource changes to `OnJob`

You will first need a program to create jobs, accounts, and resources. Compose these programs 
while enforcing the above business rules.

## Lesson 6
Write an interpreter for `EventStore` using an in memory data structure. 
Keep it simple, `List` et.al. will do.

## Lesson 7
The previous lessons taught us how to:
1) Write programs using our TTFI algebras
2) Combine different algebras in our programs
3) Combine programs to form more complex programs
4) Write interpreters for our algebras
5) Compose interpreters horizontally: Write interpreters in terms of interpreters 

In this lesson we will be looking at composing interpreters vertically: Write an interpreter for the 
`ResourceService` that also creates appropriate events for it's operations.

## Taking things further:
1) Experiment with the effect type. We kept it somple using `State` mostly but what if you wanted to perform operations
in parallel? `Future`, or `Task` might be better options. Look at `StateT` for this purpose.

2) What about error handling? What different ways can you think of? Should we have an explicit effect type to 
deal with error handling?

3) Lesson 4: `monix-kafka` provides a reactive interface to Kafka. Rewrite the in-memory interpreter for `EventStore` 
using `monix-kafka`

4) CQRS - For the most part we've ignored the read part of an event sourced Microservice architecture. 
Write a service that listens to the published events and stores a relational model in a DB for easy querying. 

5) One drawback of TTFI compared to `Free` is the 'vertical' composability of interpreters. Can you come up with a way
of composing interpreters similarly to `Free`. E.g.
```scala
val horizontal = interp1 andThen interp2
val vertical = interp1 and interp2
```

6) Add a service gateweay using finch or http4s or your weapon of choice to expose the functionality of our service.

7) SOLID principle - Convince yourself that the TTFI pattern (and `Free` Monads) address every aspect of 
SOLID. 
