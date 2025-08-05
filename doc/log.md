- Setting up environment for dev / staging / testing.
  - At first, I can run the server manually in my ec2 dev instance.
    The telegram webhook needs to hit the server which must hist the db and the temporal instance. It is cheapest to run all these on the same instance.
  - I can just manually build and start the server as I wish, using docker compose. That should keep things simple.
  - For production, I could run a k8s cluster. I probably won't need prod any time soon
- It\'s hard to develop because I need to run it on a public ip (at least for the http server) for telegram to hit it, but intellij doesn't work well over ssh.
  - I could just reverse a port back to my local
- Need to design the worker
  - For performance and durability I would want to minimize the number of components involved, especially custom components
  - The worker should have some activities
    - get response
    - future event CRUD
  - How to combine ai agent with temporal? Should it be able to do tasks in parallel? And interrupt tasks?
- Temporal or task queue? Extended Rabbit MQ? Temporal seems ideal, and it is interesting to explore this technology more
- Arch recap:
  - message sent to webhook
  - http server initiates workflow with chat id and message
  - worker loads chat history
  - worker gets next message plus next actions from LLM
    - next action may be a future message
  - worker sends message to chat
  - worker will queue the next message to send if it exists
- In the future we may add more memory, scheduled messages, multiple simultaneous messages on a timer. For now let us focus on just one future message at a time.
- I have a basic design going for the agentic workflow implementation
  - Need to figure out invocation of the workflow. 
    - When new message is received:
      - start workflow if it is not already running
      - otherwise send a signal of the next message
  - Need to figure out message streaming i.e. multiple short messages from llm instead of one big one

- To make it testable, I need to decouple from the Telegram API for local testing.
- I want to make the core features of the system invokable in a synchronous manner. I still need Temporal for
  the timers and schedules. Maybe there is simply no perfect solution here.
  - I wonder about possibilities other than Temporal. I want to have a list of timers / schedules that the AI can
    manage. 
  - Temporal might not be ideal just for timers and schedules, but it is a very flexible framework, so I may be able to
    do interesting things with it in the future.
- Let's review the roadmap
- How the server should work
  - getChat: get chat log from db
  - sendMessage: start or update workflow with message. should send message text, chat id, and webhook for push notification
- We'll need a vapid key for the push updates. Seems complicated. This part can be added later
- I need to just work out some of the communication schemas between various things. 
- First though I want to just get the worker running and get the server running even if they don't do anything
  - running `docker up` should run a server and run a worker and run the temporal system and run postgres.
    - and run migrations. we can do that in another step. we don't even need to connect to pg yet. I just want the 
      skeleton to be working now

2025-07-29T19:08:43.782Z
- docker compose up runs without error
- I need to make the app run and I need to make the temporal server run because it stopped and the app just ran jshell
  ```
  temporalio-1  | TEMPORAL_ADDRESS is not set, setting it to 172.18.0.4:7233
  temporalio-1  | Unsupported driver specified: 'DB=postgresql'. Valid drivers are: mysql8, postgres12, postgres12_pgx, cassandra.
  app-1         | |  Welcome to JShell -- Version 21
  app-1         | |  For an introduction type: /help intro
  app-1         |
  app-1         | jshell>
  app-1 exited with code 0
  ```
- fix: set postgres tag to 1.17 and changed db to postgres12 and set TEMPORAL_ADDRESS to 0.0.0.0
  
  now:
  ```
  temporalio-1  | 2025-07-30T02:14:21.589Z        ERROR   sql handle: unable to refresh database connection pool  {"error": "unable to connect to DB, tried default DB names: postgres,defaultdb, errors: [pq: password authentication failed for user \"postgres\" pq: password authentication failed for user \"postgres\"]", "logging-call-at": "/home/runner/work/docker-builds/docker-builds/temporal/common/persistence/sql/sqlplugin/db_handle.go:105"}
  temporalio-1  | 2025-07-30T02:14:21.589Z        WARN    sql handle: did not refresh database connection pool because the last refresh was too close     {"min_refresh_interval_seconds": 1, "logging-call-at": "/home/runner/work/docker-builds/docker-builds/temporal/common/persistence/sql/sqlplugin/db_handle.go:95"}
  temporalio-1  | 2025-07-30T02:14:21.589Z        ERROR   Unable to create SQL database.  {"error": "no usable database connection found", "logging-call-at": "/home/runner/work/docker-builds/docker-builds/temporal/tools/sql/handler.go:69"}
  ```
- ```
  temporalio-1  | 2025-07-30T05:58:24.575Z        ERROR   sql handle: unable to refresh database connection pool  {"error": "unable to connect to DB, tried default DB names: postgres,defaultdb, errors: [pq: the database system is starting up pq: the database system is starting up]", "logging-call-at": "/home/runner/work/docker-builds/docker-builds/temporal/common/persistence/sql/sqlplugin/db_handle.go:105"}
  temporalio-1  | 2025-07-30T05:58:24.575Z        WARN    sql handle: did not refresh database connection pool because the last refresh was too close     {"min_refresh_interval_seconds": 1, "logging-call-at": "/home/runner/work/docker-builds/docker-builds/temporal/common/persistence/sql/sqlplugin/db_handle.go:95"}
  temporalio-1  | 2025-07-30T05:58:24.576Z        ERROR   Unable to create SQL database.  {"error": "no usable database connection found", "logging-call-at": "/home/runner/work/docker-builds/docker-builds/temporal/tools/sql/handler.go:69"}
  ```
- (that is part of the same error. does docker compose auto restart containers?)
- fix was to use POSTGRES_PWD instead of POSTGRES_PASSWORD in the temporal container in docker compose
- `temporalio-1  | Unable to create dynamic config client. Error: unable to validate dynamic config: dynamic config: config/dynamicconfig/development.yaml: stat config/dynamicconfig/development.yaml: no such file or directory`
- fixed by copying files and creating that file in particular
- `temporalio-1  | time=2025-07-31T01:34:46.807 level=ERROR msg="failed reaching server: connection error: desc = \"transport: Error while dialing: dial tcp 0.0.0.0:7233: connect: connection refused\""`
- fixed: had to set TEMPORAL_ADDRESS=temporal:7233, not TEMPORAL_ADDRESS=localhost:7233 or TEMPORAL_ADDRESS=0.0.0.0:7233
- for debugging, run docker build --progress=plain .
- now I need my telegram bot token to be optional. Later I will want to somehow pass this token through docker compose
- fixed server.port
- `app-1  | Caused by: java.lang.IllegalArgumentException: Not a managed type: class com.haynesgt.agentic.common.ChatMessageEntity`
  added @EntityScan(basePackages = "com.haynesgt.agentic.common") to the AgenticServerApplication
- `app-1  | Caused by: jakarta.persistence.PersistenceException: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is java.lang.RuntimeException: Driver org.postgresql.Driver claims to not accept jdbcUrl, jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PASS}/agentic`
  fixed by removing POSTGRES_PASS from place where port should be and using POSTGERS_PORT instead.
- `app-1  | Caused by: org.postgresql.util.PSQLException: FATAL: database "agentic" does not exist`
  Need to create some scripts to run when the database starts up. Maybe use flyway
- I would want both the worker and the server to connect to the DB I think. Or should we have a dedicated server to
  save/load data? I think that would just complicate things. So both the server and the worker should have Flyway,
  and auto migrate on start if needed
- I'll remove flyway for now and just use an init SQL script
- I have the server starting up and running now. I need to add some CRUD functionality to manage chats.
  - save chat message from user or agent
  - load chat history
- It would be helpful to draw this out.