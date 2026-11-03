# Reading the code

Read the classes in this order:

1. `Asset`: a small class with a private field, constructor, and getter, similar to BankAccount or Rectangle.
2. `Severity` and `Status`: enums are fixed sets of named values. They prevent spelling mistakes such as an unknown severity string.
3. `IncidentException`: a custom runtime exception for invalid domain actions. The menu catches it and prints its message.
4. `Incident`: the abstract parent. It stores common data, validates construction, controls status changes, and declares the methods that subclasses must implement.
5. `PhishingIncident` and `MalwareIncident`: like Dog and Cat, both inherit shared behavior and override methods to provide different behavior.
6. `IncidentFactory`, `PhishingFactory`, and `MalwareFactory`: follow the practice sheet's Factory Method structure. The menu uses an abstract factory reference, not concrete incident constructors.
7. `IncidentManager`: owns an ArrayList, searches with loops, prevents duplicate IDs, returns list copies, and sorts with Comparable.
8. `IncidentStorage`: reads/writes seven tab-separated fields. Try-with-resources closes readers and writers even if an exception occurs. A temporary file prevents partial writing from directly truncating the existing file.
9. `ConsoleMenu`: uses Scanner and a switch for user choices. It creates objects through factories and calls domain/service/storage methods.
10. `Main`: starts the console menu.
11. `CyberGuardTest`: JUnit's `@Test` marks tests; assertions check expected behavior. `@TempDir` isolates test files from real incident data.

## Concepts added to meet the assignment

The provided Java examples do not show every required feature. Maven, JUnit, enums, Comparable, file I/O, custom exceptions, and try-with-resources are included because the assignment requires or supports them. No streams, lambda-based application logic, framework, GUI, or database is used.

## Useful things to explain in a demonstration

- Why fields are private and why status changes use a method instead of a public field.
- How the same Incident variable returns different advice for phishing and malware.
- Why a factory exists, and what changes when adding another type.
- How Comparable controls sorting and why getSortedIncidents returns a copy.
- What happens if a file is missing, corrupt, or cannot be saved.
- Why resolving an OPEN incident directly is rejected.
