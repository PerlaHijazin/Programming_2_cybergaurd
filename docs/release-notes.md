# v1.0.0

## Features

- Console app to record phishing and malware incidents.
- Severity changes, sorting by severity, and filtering by status.
- Enforced OPEN → INVESTIGATING → RESOLVED workflow.
- Type-specific response advice through runtime polymorphism.
- JSON persistence (`data/incidents.json`) with clear handling of missing or invalid files.
- Factory Method pattern, UML diagram, user stories, and 6 JUnit tests.

## Known limitations

Single local user; no threat detection, login, encryption, deletion, or concurrent editing. Two incident types only. Resolved incidents cannot be reopened. Force-closing loses unsaved changes.
