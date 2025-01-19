# Coding Conventions for Java Test Automation Repository

This document outlines the coding conventions and guidelines for maintaining a consistent and professional Java codebase while focusing on learning, solving real-world problems, and becoming an expert in test automation.

---

## **General Guidelines**

1. **Code Readability**:
   - Use clear and descriptive variable, class, and method names.
   - Keep methods concise and focused on a single responsibility.
   - Add meaningful comments where necessary, avoiding redundant comments.

2. **Code Structure**:
   - Organize packages based on functionality or domain (e.g., `Algorithms`, `Collections`, `DataFormatting`).
   - Group related classes and interfaces logically.

3. **Testing Philosophy**:
   - Write unit tests for all utility classes and methods.
   - Use descriptive test method names, such as `testShouldReturnSortedArray_WhenArrayHasUnorderedElements`.
   - Follow a consistent test format: Arrange, Act, Assert (AAA).

4. **Error Handling**:
   - Always handle exceptions gracefully.
   - Use custom exception messages to aid debugging.

---

## **Java-Specific Conventions**

1. **Naming Conventions**:
   - Class names: PascalCase (e.g., `HttpClient`)
   - Method names: camelCase (e.g., `getFormattedDate`)
   - Constants: UPPER_SNAKE_CASE (e.g., `DEFAULT_TIMEOUT`)

2. **Type Safety**:
   - Use generics where applicable to avoid unchecked warnings.
   - Prefer `Optional` over null checks.

3. **Best Practices**:
   - Use Streams and Lambdas for concise, functional programming.
   - Leverage `java.time` for date and time operations instead of legacy APIs.
   - Use `CompletableFuture` or the `java.util.concurrent` package for concurrency.

4. **Dependency Management**:
   - Maintain `pom.xml` or `build.gradle` with minimal, necessary dependencies.
   - Use dependency scopes appropriately (`test`, `compile`, `runtime`).

---

## **Real-World Problem Practices**

1. **Algorithms**:
   - Solve problems using clean and optimized code.
   - Include both time and space complexity analysis as comments.

2. **File Operations**:
   - Use `java.nio` for file handling instead of `java.io` for better performance.
   - Ensure all file operations are closed properly using try-with-resources.

3. **Concurrent Programming**:
   - Use `Executors` for managing thread pools.
   - Test for race conditions and deadlocks in concurrency-related code.

4. **JSON Handling**:
   - Use libraries like Jackson or Gson for serialization/deserialization.
   - Validate JSON structure before processing.

---

## **Test Automation Conventions**

1. **Framework Standards**:
   - Follow TestNG best practices, such as grouping tests and using `@DataProvider` for parameterization.
   - Configure `testng.xml` files for logical test suite execution.

2. **Report Portal Integration**:
   - Ensure `reportportal.properties` is updated with correct configurations.
   - Verify all tests log meaningful information for debugging in the Report Portal.

3. **Mocking and Stubbing**:
   - Use Mockito for mocking dependencies.
   - Avoid stubbing unnecessary methods to reduce test complexity.

4. **Performance Testing**:
   - Use JMH for micro-benchmarking critical code sections.
   - Simulate parallel HTTP requests using `ParallelHttpRequests`.

---

## **Learning Path Recommendations**

1. **Core Concepts**:
   - Practice `Generics`, `Collections`, and `Concurrency` with real-world scenarios.
   - Experiment with `Enums`, `Optionals`, and `Streams`.

2. **Advanced Topics**:
   - Explore `ModernJavaInAction` for best practices.
   - Implement solutions for `MathSolutions` and `ComplexJsonConversions`.

3. **Test Automation Expertise**:
   - Create custom utilities (e.g., `CSVUtils`, `DateUtils`) to enhance automation.
   - Focus on real-world automation challenges like `DuplicateRecords` and `FileReaders`.

---

## **Version Control**

1. **Branching Strategy**:
   - Use `main` for production-ready code.
   - Use `feature/{feature-name}` for new features.
   - Use `bugfix/{issue-id}` for bug fixes.

2. **Commit Messages**:
   - Follow the format: `[Module]: [Brief Description]`
   - Example: `CSVUtils: Add method for parsing multi-line records.`

---

By adhering to these conventions, we aim to foster a high-quality codebase, enhance learning, and build practical solutions for real-world test automation challenges.

