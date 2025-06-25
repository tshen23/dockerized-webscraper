# Java Unit Tests Base Template


## Project Structure

```
├── src/
│   ├── main/java/com/example/todo/
│   │   ├── TodoApp.java         # Main application entry point
│   │   ├── Todo.java            # Todo model class
│   │   └── TodoService.java     # Business logic service
│   └── test/java/com/example/todo/
│       ├── TodoTest.java        # Unit tests for Todo model
│       └── TodoServiceTest.java # Unit tests for TodoService
├── pom.xml                      # Maven project configuration
├── Dockerfile                   # Docker configuration
├── build_docker.sh             # Docker build script
├── run_tests.sh                # Test execution script
└── README.md                   # This file
```

## Features

- **Java 17** with Maven build system
- **JUnit** for unit testing
- **AssertJ** for fluent assertions
- **Docker** support for containerized testing

### Docker Usage

1. **Build the Docker image**:

   ```bash
   ./build_docker.sh [image-name] [platform]
   ```

   Examples:

   ```bash
   ./build_docker.sh                           # Uses default: java-unit-tests-base
   ./build_docker.sh my-java-app               # Custom image name
   ```

2. **Run tests in Docker**:

   ```bash
   ./build_docker.sh my-java-app
   docker run --rm my-java-app ./run_tests.sh
   ```

3. **Interactive Docker session**:

   ```bash
   docker run -it java-unit-tests-base
   ```

   Inside the container, you can run:

   ```bash
   mvn test                         # Run all tests
   mvn test -Dtest=YourTestClass     # Run specific test class
   ./run_tests.sh                    # Alternative test runner
   mvn exec:java -Dexec.mainClass="your.package.YourMainClass"  # Run app
   ```

### Required Template Structure

Keep these key files and structure for your project:

- **`src/main/java/`** - Your main application code (replace `com/example/todo/` with your package structure)
- **`src/test/java/`** - Your unit tests (matching your main code package structure)
- **`pom.xml`** - Maven configuration (update for your project details)
- **`Dockerfile`** - Docker configuration for containerized testing. LEAVE AS IS.
- **`build_docker.sh`** - Docker build script. LEAVE AS IS
- **`run_tests.sh`** - Test execution script. LEAVE AS IS.


## Adapting This Template for Your Project

### Replacing the Example Code

1. **Replace the package structure**:
   - Replace `src/main/java/com/example/todo/` with your own package structure
   - Example: `src/main/java/com/yourcompany/yourapp/`

2. **Update the main class**:
   - Replace the example main class with your application's entry point
   - Update the `exec.mainClass` in `pom.xml` to point to your main class

3. **Replace the example tests**:
   - Replace files in `src/test/java/com/example/todo/` with your own test classes
   - Maintain the same directory structure as your main code

4. **Update Maven configuration**:
   - Modify `pom.xml` to reflect your project details (groupId, artifactId, name)
   - Add any additional dependencies your project needs

**Important**: Keep the template files (`Dockerfile`, `build_docker.sh`, `run_tests.sh`, `pom.xml`) but update their content as needed for your project.

# Running Normally


## Install Maven
Download Maven:
- Go to https://maven.apache.org/download.cgi
- Unzip it somewhere like C:\Program Files.

Add mvn to your PATH:
- Open Windows Settings → “Environment Variables.”
- Under “System variables,” find and select Path → Edit → New.
- Paste the Maven bin folder (for example):

```
C:\Program Files\maven-mvnd-1.0.2-windows-amd64\mvn\bin
```
- Click OK on all dialogs to save.

## Compile

Open a terminal in the project root directory and run:
mvn clean package

This will produce `target/dockerized-webscraper-1.0.0.jar`.

## Run

After building, execute:
java -jar target/dockerized-webscraper-1.0.0.jar