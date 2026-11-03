# v1.0.0 - Draft release notes

Review these notes and remove "Draft" when publishing the verified final release.

## Features

- Console-based phishing and malware incident recording.
- Severity assignment and updates, severity sorting, and status filtering.
- Validated OPEN -> INVESTIGATING -> RESOLVED workflow.
- Type-specific response advice through runtime polymorphism.
- UTF-8 file persistence with validation and safe handling of corrupt records.
- Factory Method pattern, UML diagram, user stories, and JUnit tests.

## Known limitations

Single local user; no automatic threat detection, login, encryption, concurrent edits, deletion, or analyst assignment. Only phishing/malware types are supported. Tabs and line breaks are not allowed in text fields. Use explicit save or controlled exit to preserve updates. Resolved incidents cannot reopen.

## Verification before publishing

Run `mvn clean test`, `mvn package`, and a manual create/save/restart scenario from a clean clone. Check that the instructor has repository access and the final tag points to the submitted commit.
