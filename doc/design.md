# Purpose

Implementing this will allow me to create interesting AI chatbots, able to message me at its own initiative.

# Use Cases

## The User Begins Chat with Agentic

User: /start

Agent: Let me know what you need. We can talk or set up a schedule for regular discussions.

## The user suggests to discuss something later

### Message Interface

User: Let's talk about this later

Agent: I've set up a reminder

### Backend

User message + system prompt + memory -> llm -> response + memory updates + reminder creation

### Notes

The agent should be able to see what reminders and schedules it has set

## The user Indicates they want to schedule regular discussions

### Message Interface

User: Let's meet every morning at 8 am

Agent: I've set up a schedule.

### Backend

User message + system prompt + memory -> llm -> response + memory updates + schedule creation

# Roadmap

## To Do

### High Priority

#### Simple LLM Telegram Bot

### Medium Priority

#### Add basic reminders + scheduling

#### Add basic memory

## Done

#### Basic Server

#### Basic Async Worker System
