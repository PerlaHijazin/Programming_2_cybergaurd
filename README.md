# CyberGuard - Cybersecurity Incident Manager

Contributor: **Perla Hijazin**. MIT copyright identifier: **AMY253**.

CyberGuard is a console application for a small security team or student analyst who needs to record cybersecurity incidents and track their progress. It stores phishing and malware reports, their affected assets, severity, and investigation status between runs.

This is a manual incident record manager. It does not scan devices or detect attacks.

## Prerequisites

- JDK 17 or newer (a JDK, not just a JRE)
- Maven 3.6.3 or newer, or IntelliJ IDEA's bundled Maven

No database or production third-party Java libraries are required. Maven needs internet access on the first build to download build plugins and JUnit.

## Open in IntelliJ IDEA

1. Extract the project ZIP if necessary.
2. In IntelliJ, choose **File > Open** and select the `CyberGuard` folder containing `pom.xml`.
3. If asked, open it as a Maven project and trust it after reviewing the files.
4. Set **File > Project Structure > Project SDK** to JDK 17 or newer.
5. Allow Maven to finish importing. Use the Maven tool window's reload button if needed.
6. Open `src/main/java/cyberguard/Main.java` and run its `main` method using the green arrow.
7. Set the run configuration's **Working directory** to this project's folder. Data paths are relative to that folder.
8. To run tests, open the Maven tool window, expand **Lifecycle**, and run **test**.
9. To build the runnable JAR, run **package** in the same Maven tool window.

## Build, test, and run from a terminal

Run these commands from the project folder:

```sh
mvn clean test
mvn package
java -jar target/cyberguard-1.0.0.jar
```

If the terminal cannot find `mvn`, use IntelliJ's Maven tool window or install Maven and add its `bin` directory to PATH. No Maven wrapper is included.

## Sample usage

Select `1` to add an incident, then enter:

```text
Type: 1
Title: Fake university login
Description: An email asks the user to enter their password
Affected asset: Student laptop
Severity: 3
```

The program creates incident #1 with type PHISHING, severity HIGH, and status OPEN.

- Choose `2` to list incidents, highest severity first.
- Choose `3`, enter ID `1`, and enter `4` to change its severity to CRITICAL.
- Choose `4`, enter ID `1`, and enter `2` to begin investigating.
- Choose `4` again, enter ID `1`, and enter `3` to resolve it.
- Choose `5` and enter `3` to list resolved incidents.
- Choose `6` to save, or `0` to save and exit.
- Run the application again to see the saved incidents restored.

If you enter invalid input, the action is cancelled with a message; choose the menu option again to retry. An incident must progress through OPEN -> INVESTIGATING -> RESOLVED. It cannot skip investigation or reopen after resolution. Selecting the existing status is allowed.

## Implemented requirements

| Requirement | Implementation |
| --- | --- |
| Encapsulation | Private fields, constructors, getters, controlled severity and status methods |
| Abstraction | Abstract `Incident` and `IncidentFactory` classes |
| Inheritance | `PhishingIncident` and `MalwareIncident` extend `Incident` |
| Runtime polymorphism | A loop over `Incident` calls overridden `getResponseAdvice()` and `getType()` |
| Relationships | A manager aggregates 0..* incidents; each incident holds one affected `Asset` |
| Collections and generics | `ArrayList<Incident>` |
| Sorting | `Comparable<Incident>`, critical first and then ascending ID |
| Domain exception | `IncidentException` validates fields, IDs, duplicates, and status transitions |
| Persistence | UTF-8 tab-separated text, try-with-resources, validation on load |
| Pattern | Factory Method through two concrete factories |
| Console UI | `ConsoleMenu` with numbered choices |
| Build | Maven, JDK 17+, executable JAR |
| Tests | JUnit 5 domain, manager, factory, and storage tests |

## Factory Method pattern

`IncidentFactory` declares `createIncident(...)`. `PhishingFactory` and `MalwareFactory` override it and return the appropriate concrete incident through the common `Incident` type. `ConsoleMenu` chooses a factory at runtime and calls the same method regardless of the chosen type. `IncidentStorage` uses the same factories when rebuilding saved records.

This keeps the construction of each incident type in its factory and keeps the menu's creation call independent of concrete incident constructors. Adding a type requires a new incident subclass, a new factory, and updates to the menu and loader's type selection. This is intentionally similar to the class exercise's character factories.

## Project structure

```text
src/main/java/cyberguard/
  Main.java
  domain/       Incident, concrete incidents, Asset, Severity, Status, exception
  service/      IncidentManager and factories
  persistence/  IncidentStorage
  ui/           ConsoleMenu
src/test/java/cyberguard/CyberGuardTest.java
docs/           UML, user stories, design notes, GitHub guide, release notes
```

See [UML diagram](docs/uml.svg), [editable UML source](docs/uml.mmd), [user stories](docs/user-stories.md), [design notes](docs/design.md), and [code walkthrough](docs/code-walkthrough.md).

## Persistence and limitations

Records are saved to `data/incidents.txt` relative to the working directory. The folder is created on the first save and excluded from Git. A header identifies the format; each record stores ID, type, title, description, asset, severity, and status. Tabs and line breaks are forbidden in free-text fields so that the simple file format remains unambiguous.

The entire file is validated before any loaded records are used. A missing file starts a new empty collection. Invalid or duplicate records stop startup with an error, protecting the existing file from accidental overwrite. Save writes a temporary file before replacing the previous file. This is not a database transaction or a backup system.

This version supports one local user, two incident types, one asset per incident, and no authentication, deletion, assignment to analysts, encryption, or concurrent edits. Save with option 6 or exit with option 0; forcibly closing the program can lose changes since the last save. Records may contain sensitive information: use fictional examples for coursework.

## Tests

`mvn clean test` executes six JUnit tests covering valid and invalid object creation, Factory Method polymorphism, status transitions, sorting and filtering, duplicate IDs, file round trips for both types, and corrupt/missing file handling.

See [verification results](docs/verification.md) for the checks performed and a local Maven compiler restriction.

## Repository and submission

The local code package does not create a GitHub repository, share access, create issues/PRs, or publish a release. Follow [the GitHub guide](docs/github-steps.md) for those required steps. The assignment states that the instructor should have access before development begins. Discuss the timing with the instructor if that step has not been completed.

The assignment calls testing optional in one section but asks for passing JUnit tests in the submission checklist. Tests are included to satisfy the stricter checklist. The stated deadline is **29 November 2026**.

License: [MIT](LICENSE).
