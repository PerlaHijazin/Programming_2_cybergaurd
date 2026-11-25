# CyberGuard – Cybersecurity Incident Manager

**Contributor:** Perla Hijazin (Neptun code: AMY253)

CyberGuard is a console application for a small security team or student analyst who needs to record cybersecurity incidents and track their progress. It stores phishing and malware reports, the affected asset, severity, and investigation status, and keeps them between runs in a JSON file.

It is a manual record manager: it does not scan devices or detect attacks.

## Prerequisites

- JDK 17 or newer
- Maven 3.6.3 or newer (or IntelliJ IDEA's bundled Maven)

Maven downloads Gson (JSON library) and JUnit on the first build, so the first build needs internet access.

## Build, test, and run

From the project folder (the one containing `pom.xml`):

```sh
mvn clean test       # run the JUnit tests
mvn clean package    # build the runnable JAR
java -jar target/cyberguard-1.0.0-jar-with-dependencies.jar
```

**In IntelliJ IDEA:** File > Open > select this folder, open as a Maven project, set the Project SDK to JDK 17+, then run `src/main/java/cyberguard/Main.java`. Tests: Maven tool window > Lifecycle > `test`.

Data is saved to `data/incidents.json` relative to the working directory (the project folder when run as above).

## Sample usage

Choose `1` to add an incident, then enter:

```text
Type (1 Phishing, 2 Malware): 1
Title: Fake university login
Description: An email asks the user to enter their password
Affected asset (for example, Office PC): Student laptop
Severity (1 LOW, 2 MEDIUM, 3 HIGH, 4 CRITICAL): 3
```

The program creates incident #1 (PHISHING, HIGH, OPEN).

- `2` lists incidents, highest severity first, with type-specific advice.
- `3` → ID `1` → `4` changes severity to CRITICAL.
- `4` → ID `1` → `2` starts the investigation; repeat with `3` to resolve it.
- `5` → `3` lists resolved incidents.
- `6` saves, `0` saves and exits. Run the program again and the incidents are restored.

The saved file looks like this:

```json
[
  {
    "id": 1,
    "type": "PHISHING",
    "title": "Fake university login",
    "description": "An email asks the user to enter their password",
    "asset": "Student laptop",
    "severity": "CRITICAL",
    "status": "RESOLVED"
  }
]
```

Invalid input cancels the current action with a message. Status must move OPEN → INVESTIGATING → RESOLVED; it cannot skip a step or go back.

## Implemented requirements

| Requirement | Where |
| --- | --- |
| Encapsulation | All fields `private` (most `final`); state changes only through `setSeverity()` and `changeStatus()`, which validate input |
| Abstraction | Abstract classes `Incident` and `IncidentFactory` |
| Inheritance & runtime polymorphism | `PhishingIncident` and `MalwareIncident` extend `Incident` and override `getType()` / `getResponseAdvice()`; the menu only works with `Incident` references |
| Relationships | `IncidentManager` 1 → 0..* `Incident` (aggregation); `Incident` 0..* → 1 `Asset` (association); `ConsoleMenu` composes a manager and a storage |
| Collections & generics | `List<Incident>` / `ArrayList<Incident>` |
| Sorting | `Incident implements Comparable<Incident>` (critical first, then by ID); a lambda filters by status |
| Custom exception | `IncidentException` (unchecked) for domain rules: empty fields, invalid IDs, duplicate IDs, illegal status changes |
| Persistence | JSON file via Gson, `try-with-resources`, invalid files reported clearly |
| Design pattern | Factory Method (below) |
| Build | Maven, JDK 17+ |
| Tests | 6 JUnit 5 tests (`mvn clean test`) |

## Design pattern: Factory Method

**Problem:** the menu (when the user adds an incident) and the storage (when it rebuilds incidents from JSON) both need to create the right kind of incident without depending on concrete constructors.

**Participants:**
- `IncidentFactory` (abstract creator) declares `createIncident(...)` and has `forType(String)` which returns the right factory for a type name.
- `PhishingFactory` and `MalwareFactory` (concrete creators) override `createIncident(...)` and return a `PhishingIncident` or `MalwareIncident`.
- `Incident` is the product type that the rest of the program uses.

**Benefit:** `ConsoleMenu` and `IncidentStorage` share one creation path, and only work with `Incident`. Adding a new type means one new subclass, one new factory, and one line in `forType`.

## Project structure

```text
src/main/java/cyberguard/
  Main.java
  domain/       Incident, PhishingIncident, MalwareIncident, Asset, Severity, Status, IncidentException
  service/      IncidentManager, IncidentFactory, PhishingFactory, MalwareFactory
  persistence/  IncidentStorage (JSON)
  ui/           ConsoleMenu
src/test/java/cyberguard/CyberGuardTest.java
docs/           UML diagram (uml.md, rendered by GitHub; source uml.mmd), user stories, design notes, release notes
```

See the [UML diagram](docs/uml.md), [user stories](docs/user-stories.md), [design decisions](docs/design.md), and [code walkthrough](docs/code-walkthrough.md).

## Tests

`mvn clean test` runs six JUnit 5 tests: factory polymorphism, validation, status workflow, sorting/filtering/IDs, JSON save and load for both types, and missing/invalid file handling.

## Known limitations

One local user; two incident types; one asset per incident; no deletion, login, encryption, or concurrent editing. Changes are saved with option `6` or `0` (or when input ends); force-closing the program loses unsaved changes. Use fictional data for coursework.

## License

[MIT](LICENSE) – copyright identifier AMY253.
