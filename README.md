# Autonomous Driving Scenario Manager

A Java desktop application for creating, organizing, and reviewing simplified autonomous-driving scenarios. It combines an object-oriented domain model, JSON persistence, event logging, a console interface, and a Java Swing graphical interface.

> This is an educational scenario-management prototype. Its rule-based risk labels are illustrative and must not be used for real vehicle or safety decisions.

## Features

- Create and remove driving scenarios
- Store weather, road, speed, obstacle, and distance information
- Assign `LOW`, `MEDIUM`, or `HIGH` risk labels with a transparent rule-based model
- Filter scenarios by risk level in the console interface
- Save and load the scenario library as JSON
- Record scenario additions and removals in an application event log
- Use either a Swing GUI or a console workflow
- Verify the model and persistence layers with JUnit tests

## Project structure

| Package | Responsibility |
| --- | --- |
| `model` | Driving scenarios, scenario collections, risk classification, and event logging |
| `persistence` | JSON serialization and deserialization |
| `ui` | Swing GUI, console interface, and application entry point |
| `src/test` | Unit tests for the model and persistence layers |

## Risk model

The current prototype calculates a base safe distance as half of the vehicle speed. Rain or fog increases that distance by 50%. An obstacle inside half of the adjusted safe distance is labelled `HIGH`; one inside the full adjusted distance is labelled `MEDIUM`; otherwise it is labelled `LOW`.

The calculation assumes that speed and distance inputs use a consistent scenario-specific convention. A production model would require physically meaningful units and factors such as relative velocity, time to collision, braking distance, road conditions, and uncertainty.

## Run locally

Requirements:

- JDK 17 or newer
- Apache Maven 3.9 or newer

From the repository root:

```bash
mvn clean test
mvn exec:java
```

The application reads and writes `data/scenarioLibrary.json`.

## Background and attribution

This project was originally developed as a UBC CPSC 210 software-construction course project and was later reorganized as a standalone portfolio repository. The event-log classes draw on the course AlarmSystem example, and the JSON persistence structure draws on the course JsonSerializationDemo; those relationships are also documented in the relevant source files.

