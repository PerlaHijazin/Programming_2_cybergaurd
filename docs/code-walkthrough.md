# Reading the code

Suggested order:

1. `Asset` – one private field, a validating constructor, and a getter.
2. `Severity` and `Status` – enums. `Status.canMoveTo()` says which status changes are allowed.
3. `IncidentException` – custom unchecked exception for broken domain rules.
4. `Incident` – abstract parent: common fields, validation, `changeStatus()`, sorting (`compareTo`), and the abstract methods `getType()` / `getResponseAdvice()`. `toString()` calls those abstract methods, so each subclass prints differently.
5. `PhishingIncident`, `MalwareIncident` – override the abstract methods.
6. `IncidentFactory`, `PhishingFactory`, `MalwareFactory` – the Factory Method pattern.
7. `IncidentManager` – keeps the `List<Incident>`, prevents duplicate IDs, sorts, and filters with a lambda (`removeIf`).
8. `IncidentStorage` – converts incidents to simple records and writes them as JSON with Gson; on load it uses the factories to rebuild the right subclass. `try-with-resources` closes the file automatically.
9. `ConsoleMenu` – reads user input with `Scanner`, calls the service and storage, and prints messages for errors.
10. `Main` – starts the menu.
11. `CyberGuardTest` – JUnit 5 tests; `@TempDir` keeps test files away from real data.

## Things to be ready to explain

- Why fields are private and status changes go through a method.
- How one `Incident` variable gives different advice for phishing and malware.
- What the factory gives us, and what changes when a new type is added.
- How `Comparable` controls sorting, and why the manager returns copies.
- What happens when the JSON file is missing or invalid.
