# Scalable System Design Patterns

- [Scalable System Design Patterns](#scalable-system-design-patterns)
  - [Load Balancer](#load-balancer)
  - [Scatter and Gather](#scatter-and-gather)
  - [Result Cache](#result-cache)
  - [Shared Space](#shared-space)
  - [Pipe and Filter](#pipe-and-filter)
  - [Map Reduce](#map-reduce)
  - [Bulk Synchronous Parallel](#bulk-synchronous-parallel)
  - [Execution Orchestrator](#execution-orchestrator)
  - [References](#references)

## Load Balancer

- There is a dispatcher that determines which worker instance will handle the request based on different policies.
- The application should best be "stateless" so any worker instance can handle the request.
- This pattern is deployed in almost every medium to large web site setup.

```plantuml
@startuml
[Client] -> [Dispatcher]
[Client] <- [Dispatcher]
note top of Dispatcher
1) Pick a worker to forward request
    - Random
    - Round robin
    - Least busy
    - Sticky session / cookies
    - By request parameters
2) Wait for its response
3) Forward the response to client
end note
package "Worker Pool" {
    database "db 1"
    database "db 2"
    database "db n"
    [Worker 1] - [db 1]
    [Worker 1] .. [Worker 2]
    [Worker 2] - [db 2]
    [Worker 2] .. [Worker n]
    [Worker n] - [db n]
}
[Dispatcher] -> [Worker 2]
@enduml
```

## Scatter and Gather

- The dispatcher multi-casts the request to all workers of the pool.
- Each worker will compute a local result and send it back to the dispatcher, who will consolidate them into a single response and then send back to the client.
- This pattern is used in Search engines like Google to handle user's keyword search request, etc.

```plantuml
@startuml
[Client] -> [Dispatcher]
[Client] <- [Dispatcher]
note top of Dispatcher
1) Broadcast request to all workers.
2) Wait for all responses.
3) Consolidate all responses to a single response.
end note
package "Worker Pool" {
    database "db 1"
    database "db 2"
    database "db n"
    [Worker 1] - [db 1]
    [Worker 1] .. [Worker 2]
    [Worker 2] - [db 2]
    [Worker 2] .. [Worker n]
    [Worker n] - [db n]
}
[Dispatcher] -> [Worker 1]
[Dispatcher] <- [Worker 1]
[Dispatcher] -> [Worker 2]
[Dispatcher] <- [Worker 2]
[Dispatcher] -> [Worker n]
[Dispatcher] <- [Worker n]
@enduml
```

## Result Cache

- The dispatcher will first lookup if the request has been made before and try to find the previous result to return, in order to save the actual execution.
- This pattern is commonly used in large enterprise application.
- Memcached is a very commonly deployed cache server.

```plantuml
@startuml
[Client] -> [Dispatcher]
[Client] <- [Dispatcher]
[Dispatcher] --> [Cache]
[Cache] <-- [Dispatcher]
note top of Dispatcher
1) Lookup cache.
2) If found result, return to client.
3) Otherwise, forward to worker, store result to cache.
end note
package "Worker Pool" {
    database "db 1"
    database "db 2"
    database "db n"
    [Worker 1] - [db 1]
    [Worker 1] .. [Worker 2]
    [Worker 2] - [db 2]
    [Worker 2] .. [Worker n]
    [Worker n] - [db n]
}
[Dispatcher] .> [Worker 1]
[Dispatcher] <. [Worker 1]
[Dispatcher] .> [Worker 2]
[Dispatcher] <. [Worker 2]
[Dispatcher] .> [Worker n]
[Dispatcher] <. [Worker n]
@enduml
```

## Shared Space

- Also known as "Blackboard".
- All workers monitors information from the shared space and contributes partial knowledge back to the blackboard.
- The information is continuously enriched until a solution is reached.
- This pattern is used in JavaSpace and also commercial product GigaSpace.

```plantuml
@startuml
cloud "Tuple Space"
[Client] -> [Tuple Space]
note top of Client
1) Put request in space.
2) Take request from space.
end note
package "Worker Pool 1" {
    [Worker n]
    [Worker 2]
    [Worker 1]
}
package "Worker Pool 2" {
    [Worker m]
    [Worker 5]
    [Worker 4]
}
note as WorkerPoolNote
Repeat
1) Take intermediate result from space.
2) Add value to result.
3) Put partial result in space.
end note
[Worker n] .. WorkerPoolNote
WorkerPoolNote .. [Worker m]
[Worker n] --> [Tuple Space]
[Worker n] <-- [Tuple Space]
[Worker 1] --> [Tuple Space]
[Worker 1] <-- [Tuple Space]
[Worker 2] --> [Tuple Space]
[Worker 2] <-- [Tuple Space]
[Tuple Space] --> [Worker m]
[Tuple Space] <-- [Worker m]
[Tuple Space] --> [Worker 4]
[Tuple Space] <-- [Worker 4]
[Tuple Space] --> [Worker 5]
[Tuple Space] <-- [Worker 5]
@enduml
```

## Pipe and Filter

- This model is also known as "Data Flow Programming"; all workers connected by pipes where data is flow across.
- [This pattern](http://www.eaipatterns.com/PipesAndFilters.html) is a very common EAI pattern.

```plantuml
@startuml
note top of Client
1) Put command in request queue.
2) Take result from response queue.
end note
component "Intermediate Queue"
[Client] -> [Request Queue 1]
[Client] -> [Request Queue 2]
package "Worker Pool 1" {
    [Worker n]
    [Worker 2]
    [Worker 1]
}
[Request Queue 1] -> [Worker 1]
[Request Queue 1] -> [Worker 2]
[Request Queue 1] -> [Worker n]
package "Worker Pool 2" {
    [Worker m]
    [Worker 5]
    [Worker 4]
}
[Request Queue 2] -> [Worker 4]
[Request Queue 2] -> [Worker 5]
[Request Queue 2] -> [Worker m]
note as WorkerPoolNote
Repeat
1) Take data from input queue.
2) Processing data.
3) Put processed data in output queue.
end note
[Worker n] .. WorkerPoolNote
WorkerPoolNote .. [Worker m]
[Worker n] --> [Intermediate Queue]
[Worker 1] --> [Intermediate Queue]
[Worker 2] --> [Intermediate Queue]
[Intermediate Queue] --> [Worker m]
[Intermediate Queue] --> [Worker 4]
[Intermediate Queue] --> [Worker 5]
@enduml
```

## Map Reduce

- The model is targeting batch jobs where disk I/O is the major bottleneck.
- It uses a distributed file system so that disk I/O can be done in parallel.
- This pattern is used in many of Google's internal application, as well as implemented in open source Hadoop parallel processing framework.
- This pattern can be used in many many application design scenarios.

```plantuml
@startuml
package "DFS (Input)" {
    database "db a"
    database "db b"
    database "db c"
    Input .. [db a]
    [db a] .. [db b]
    [db b] .. [db c]
}
package "DFS (Output)" {
    database "db x"
    database "db y"
    database "db z"
    Output .. [db x]
    [db x] .. [db y]
    [db y] .. [db z]
}
package "Job Scheduler" {
    Job_Scheduler .. Mappers
    frame "Mappers" {
        [Mapper 1] .. [Mapper 2]
        [Mapper 2] .. [Mapper n]
    }
    frame "Reducers" {
        [Reducer 1] .. [Reducer n]
    }
    [Mapper 1] -[#black]> [Reducer 1]
    [Mapper 2] -[#black]> [Reducer 1]
    [Mapper n] -[#black]> [Reducer 1]
    [Mapper 1] -[#black]> [Reducer n]
    [Mapper 2] -[#black]> [Reducer n]
    [Mapper n] -[#black]> [Reducer n]
}
note as DFS
DFS = Distributed File System
end note
[db a] -> [Mapper 1]
[db b] -> [Mapper 2]
[db c] -> [Mapper n]
[Reducer 1] -> [db x]
[Reducer n] -> [db y]
[Client] --> Job_Scheduler
[Client] --> Input
Output --> [Client]
note left of Client
1) Load input data into input DFS.
2) Submit job.
3) Extract input from output DFS.
end note
@enduml
```

## Bulk Synchronous Parallel

- This model is based on lock-step execution across all workers, coordinated by a master.
- Each worker repeat the following steps until the exit condition is reached, when there is no more active workers.

1. Each worker read data from input queue
2. Each worker perform local processing based on the read data
3. Each worker push local result along its direct connection

- This pattern has been used in Google's [Pregel graph processing model](http://horicky.blogspot.com/2010/07/google-pregel-graph-processing.html) as well as the [Apache Hama](http://incubator.apache.org/hama/) project.

```plantuml
@startuml
package " " {
    frame "Worker1" {
        InQ1 - [Worker1]
    }
    frame "Worker2" {
        InQ2 - [Worker2]
    }
    frame "Worker3" {
        InQ3 - [Worker3]
    }
    frame "Worker4" {
        InQ4 - [Worker4]
    }
    frame "Worker5" {
        InQ5 - [Worker5]
    }

    [Master] <.> [Worker1]
    [Master] <.> [Worker2]
    [Master] <.> [Worker3]
    [Master] <.> [Worker4]
    [Master] <.> [Worker5]

    [Worker1] -[#black,bold]-> InQ2
    [Worker1] -[#black,bold]-> InQ3
    [Worker1] -[#black,bold]-> InQ4

    [Worker2] -[#black,bold]-> InQ3

    [Worker3] -[#black,bold]-> InQ1

    [Worker4] -[#black,bold]-> InQ5

    [Worker5] -[#black,bold]-> InQ3
    [Worker5] -[#black,bold]-> InQ4
}
[Client] <-[bold]-> [Master]
note top of Client
1) Load input data into workers.
2) Notify master to start processing.
3) Wait for master for completion.
4) Extract result data from workers.
end note
note top of Master
1) Receive "Start Processing" from client.
2) Repeat until no active workers.
    - Signal all active workers to process.
    - Wait for all workers finished.
    - Update active worker counts.
3) Notify client about completion.
end note
note bottom of Worker3
1) Set myself active.
2) Repeat until I am inactive.
    - Wait for master "start" signal.
    - Read data from InQ.
    - Perform local processing.
    - Send out data along connection.
    - Update my "active" status.
    - Notify master I am done.
3) When data arrives in InQ
    - Change myself back to "active".
end note
@enduml
```

## Execution Orchestrator

- This model is based on an intelligent scheduler / orchestrator to schedule ready-to-run tasks (based on a dependency graph) across a clusters of dumb workers.
- This pattern is used in [Microsoft's Dryad project](http://research.microsoft.com/en-us/projects/dryad/).

```plantuml
@startuml
package " " {
    frame "Channels" {
        [Channel1] .. [Channel2]
        [Channel2] .. [Channel3]
        [Channel3] .. [Channel4]
    }

    [Worker 1] -[#black]-> [Channel1]
    [Worker 2] -[#black]-> [Channel1]
    [Worker n] -[#black]-> [Channel4]
}
[Orchestrator] <-[#black,bold]- [Worker 1]
[Orchestrator] <-[#black,bold]- [Worker 2]
[Orchestrator] <-[#black,bold]- [Worker n]

package "Orchestrator" {
    frame "Task Graph" {
        () "1" -[#blue]-> () "2"
        () "2" -[#blue]-> () "3"
        () "1" -[#blue]-> () "4"
        () "4" -[#blue]-> () "5"
        () "4" -[#blue]-> () "3"
    }
}
[Client] -> Orchestrator
@enduml
```

## References

1. [Scalable System Design Patterns](http://horicky.blogspot.com/2010/10/scalable-system-design-patterns.html)
2. [Scalable System Design](http://horicky.blogspot.com/2008/02/scalable-system-design.html)
3. [Some very basic patterns underlying NOSQL](http://horicky.blogspot.com/2009/11/nosql-patterns.html)
4. [Some leading implementations](http://horicky.blogspot.com/2010/10/bigtable-model-with-cassandra-and-hbase.html)
