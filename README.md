# Othello (Reversi) — Java Backend + JavaFX MVC UI

This repository contains my Othello (Reversi) project completed in **two parts**:

- **A1 (Part 1):** Core backend logic — board representation, rules, move validation, and disk flipping. 
- **A2 (Part 2):** JavaFX front-end using an **MVC-style** structure (model + controller + JavaFX UI), plus command/history support and tests.

---

## Project Structure (high level)

The main code is organized under the following packages (from `src/`):

- `ca.yorku.eecs3311.othello.model`  
  Core Othello logic (board, game rules, players, move validation, visitors, etc.)

- `ca.yorku.eecs3311.othello.command`  
  Command objects (e.g., move command) and game history tracking

- `ca.yorku.eecs3311.othello.viewcontroller`  
  JavaFX application entry point and UI/controller wiring (Part 2)

- `ca.yorku.eecs3311.othello.test`  
  JUnit tests for key logic components

- `ca.yorku.eecs3311.mvcexample`  
  Small MVC example/demo classes used during the assignment

- `images/`  
  Any UI or project assets

---

## Features

### A1 — Backend / Game Logic
- Othello board representation
- Legal move detection
- Token flipping in all directions
- Player strategies (e.g., Human/Random/Greedy variants depending on assignment requirements)
- Visitor-style utilities (e.g., counting tokens, listing valid moves)

### A2 — JavaFX MVC UI
- JavaFX application UI for interacting with the game
- MVC-style separation between:
  - **Model**: `ca.yorku.eecs3311.othello.model`
  - **Controller / ViewController**: `ca.yorku.eecs3311.othello.viewcontroller`
- Command/history support to track gameplay actions (assignment requirement)
- JUnit tests included for verifying core correctness

---

## Requirements

- **Java (JDK):** 17+ recommended  
  (My local setup uses **OpenJDK 21**)
- **JavaFX SDK:** JavaFX 17+  
  (JavaFX is NOT bundled with the JDK, so it must be added separately)
- **IDE:** Eclipse (recommended for this project)

---

## How to Run (Eclipse)

### 1) Configure the Project JDK
In Eclipse:
- `Project → Properties → Java Build Path → Libraries`
- Ensure **JRE System Library** points to a **JDK** (ex: OpenJDK 21)

### 2) Add JavaFX Libraries
Still in:
- `Project → Properties → Java Build Path → Libraries → Add External JARs...`

Add these JARs from your JavaFX SDK `lib` folder (example path shown below):
`/Users/<your-user>/Desktop/javafx-sdk-17.0.0.1/lib`

Add at least:
- `javafx.base.jar`
- `javafx.controls.jar`
- `javafx.fxml.jar`
- `javafx.graphics.jar`

### 3) Add VM Arguments (JavaFX runtime)
Go to:
- `Run → Run Configurations... → (your app) → Arguments → VM arguments`

Set:

```bash
--module-path "/Users/<your-user>/Desktop/javafx-sdk-17.0.0.1/lib" --add-modules javafx.controls,javafx.fxml
