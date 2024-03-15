# Toy Language Interpreter in Java

This project is a comprehensive toy language interpreter implemented in Java, created as a project for my third semester at university. It is designed to execute programs written in a simplified language, handling various operations and control flow structures. This project does not rely on a lexer or parser; instead, it directly processes Abstract Syntax Trees (ASTs) provided to it.
## Features

- **Graphical User Interface (GUI):** The interpreter comes with a GUI built using JavaFX. It provides visual representations of the execution steps, illustrating how each operation affects the underlying data structures.
- **Data Structures:** The interpreter manages several essential data structures:
  - Execution Stack: Tracks the execution flow of the program.
  - Symbol Table: Stores variable declarations and assignments.
  - File Table: Manages information related to each file from which data can be read.
  - Heap: Manages memory allocation, reading, and writing.
  - Program Table: Facilitates program thread execution, updating other tables accordingly.
  - Semaphore Table: Manages synchronization between threads.
- **Supported Data Types:** The interpreter supports various data types, including `int`, `bool`, `string`, and references to other data types.
- **Operations and Control Flow:** It supports a range of operations and control flow structures, such as:
  - Variable declarations and assignments.
  - Conditional statements (`if`, `switch`).
  - Looping constructs (`while`).
  - File operations (reading).
  - Thread creation and management.
  - Arithmetic and logical operations.
- **Type Checking:** Includes a type checker to ensure type safety during program execution.
- **Garbage Collector:** Implements a garbage collector capable of handling nested references, ensuring efficient memory management.
- **Scheduler:** Enables fair execution of multiple program threads by running one instruction for each thread in a round-robin manner during each scheduling cycle.

## Usage

To use the interpreter:

1. Run the Java program.
2. Use the GUI interface to load programs written in the toy language.
3. Execute the program to observe the step-by-step execution and the impact on underlying data structures, or run it all at once to see the outputs.
4. Analyze the results to understand program behavior and data manipulation.


![](https://github.com/AlexandruCostea/Toy-Language-Interpreter/blob/master/1.png)
![](https://github.com/AlexandruCostea/Toy-Language-Interpreter/blob/master/2.png)
## Requirements

- Java Development Kit (JDK)
- JavaFX library

## Installation

1. Clone or download the repository.
2. Set up your Java development environment.
3. Ensure JavaFX is properly configured.
4. Run the main Java file containing the interpreter.
