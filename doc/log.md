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