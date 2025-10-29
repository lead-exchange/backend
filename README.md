# Backend Project

A Spring Boot-based Telegram bot application with web capabilities and comprehensive testing setup.

## Prerequisites

- Java 21
- Maven 3.8.6+
- Docker (for TestContainers in integration tests)

## Project Structure

```
src/
├── main/
│   ├── java/          # Application source code
│   └── resources/     # Configuration files
└── test/
    └── java/          # Unit and integration tests
```

## Build Commands

### Package the Application
```bash
mvn clean package
```


### Run Tests
```bash
# Run all tests (unit + integration)
mvn verify

# Run only unit tests
mvn test

# Run only integration tests
mvn failsafe:integration-test
```

### Check Code Style
```bash
mvn checkstyle:check
```
This command validates the code against the checkstyle rules defined in `checkstyle.xml` and `checkstyle-suppressions.xml`.


## Code Quality Tools

### Checkstyle
The project uses Checkstyle to maintain code quality standards. Configuration files:
- `checkstyle.xml` - Main rules configuration
- `checkstyle-suppressions.xml` - Rules suppressions

### JaCoCo Test Coverage
Test coverage reports are generated automatically during build and can be found at:
`target/site/jacoco/index.html`

