# TaskForge

## Requirements

- Java 17+

## Build & Test

```bash
./mvnw compile        # compile
./mvnw test           # run tests
```

## Run

```bash
./mvnw -q exec:java -Dexec.mainClass=taskforge.Main -Dexec.args=src/main/resources/simple.tasks
```

## Task File Format

```
compile: duration=5
test: duration=3, depends=[compile]
lint: duration=2, depends=[compile]
package: duration=4, depends=[test, lint]
```
