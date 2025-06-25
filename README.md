# Java Unit Tests


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