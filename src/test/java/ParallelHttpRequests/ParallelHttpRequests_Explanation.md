# ParallelHttpRequests Explanation

## Overview

The `ParallelHttpRequests` class demonstrates how to perform parallel HTTP requests using Java's `HttpClient` and `CompletableFuture`. This implementation allows for efficient execution of multiple HTTP requests concurrently, which can significantly reduce the overall execution time compared to sequential requests.

## How It Works

1. **Test Data Reading**: 
   - The program starts by reading test data (names) from a file specified by `TEST_DATA_FILE`.
   - Each line in the file represents a name to be used in the HTTP requests.

2. **Parallel Execution Setup**:
   - An `ExecutorService` with a fixed thread pool of 10 threads is created to manage the parallel execution.
   - An `HttpClient` is initialized for making HTTP requests.
   - An `ObjectMapper` (from Jackson library) is set up for JSON serialization.

3. **Request Processing**:
   - For each name in the test data, a `CompletableFuture` is created.
   - Each `CompletableFuture` represents an asynchronous task that will:
     a. Create a JSON payload with the name and a fixed job title ("leader").
     b. Build an HTTP POST request with the payload.
     c. Send the request to the specified endpoint.
     d. Process the response (check status code and print the response body).

4. **Parallel Execution**:
   - All `CompletableFuture` tasks are added to a list.
   - `CompletableFuture.allOf()` is used to wait for all futures to complete.
   - This allows all requests to be executed in parallel.

5. **Result Processing**:
   - As each request completes, it prints the response body and status.
   - Successful requests (status code 200) are logged differently from failed requests.

6. **Execution Time Measurement**:
   - The total execution time is measured and printed at the end.

## How Parallel Execution Works

1. **CompletableFuture**: 
   - `CompletableFuture` is used to represent a future result of an asynchronous computation.
   - It allows for non-blocking execution of tasks.

2. **ExecutorService**:
   - A fixed thread pool of 10 threads is used to manage the parallel execution.
   - This limits the number of concurrent tasks to 10, preventing overwhelming of system resources.

3. **Asynchronous Execution**:
   - Each HTTP request is wrapped in a `CompletableFuture` and submitted to the `ExecutorService`.
   - This allows multiple requests to be processed simultaneously, utilizing available CPU cores and managing I/O efficiently.

4. **Non-Blocking Operations**:
   - While waiting for HTTP responses, the threads are free to process other requests.
   - This efficient use of threads allows for handling many requests with a limited number of threads.

5. **Coordination of Results**:
   - `CompletableFuture.allOf()` is used to wait for all requests to complete before finalizing the program.
   - This ensures that all requests are processed before the program terminates.

## Benefits of Parallel Execution

1. **Improved Performance**: By executing requests in parallel, the overall execution time can be significantly reduced compared to sequential execution.

2. **Efficient Resource Utilization**: The program can efficiently utilize available CPU cores and manage I/O operations.

3. **Scalability**: The approach can easily handle a large number of requests without linear increase in execution time.

4. **Controlled Concurrency**: By using a fixed thread pool, the program prevents overwhelming the system or the target server with too many concurrent requests.

## Conclusion

This implementation demonstrates an efficient way to perform parallel HTTP requests in Java, utilizing modern Java features like `HttpClient` and `CompletableFuture`. It's particularly useful for scenarios where a large number of independent HTTP requests need to be processed quickly, such as in testing or data processing applications.