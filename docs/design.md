# Design decisions

- **Separate packages** for domain, service, persistence, and UI, as the assignment asks.
- **Abstract `Incident` with two subclasses.** Shared data and rules live in the parent; each subclass supplies its own type and response advice (runtime polymorphism).
- **Factory Method** (`IncidentFactory`, `PhishingFactory`, `MalwareFactory`) so the menu and the storage create incidents the same way and never call concrete constructors. `forType()` keeps the type-name mapping in one place.
- **Enums** for `Severity` and `Status`, so invalid values cannot enter the model. `Status.canMoveTo()` holds the workflow rule (stay, or move one step forward).
- **Validation in constructors and setters.** An `Incident` or `Asset` can never exist in an invalid state; violations throw `IncidentException`.
- **Asset as its own object** – each incident has exactly one affected asset.
- **Natural ordering** via `Comparable<Incident>`: highest severity first, then by ID. The manager returns copies of its list so outside code cannot change it.
- **IDs** are calculated from the highest existing ID, so restarting never reuses an ID.
- **JSON persistence with Gson.** The data file is human-readable. A small private `IncidentRecord` describes the JSON shape, and the factories rebuild the right subclass on load. If the file is invalid the program stops instead of overwriting it.

## Known limitations

Single local user, two incident types, one asset per incident, sequential status only, no deletion, authentication, or concurrent access. Forced shutdown loses unsaved changes. Adding a type requires a subclass, a factory, and a line in `IncidentFactory.forType`.

## UML

`uml.md` is the viewable diagram (GitHub renders it); `uml.mmd` is the editable Mermaid source. `+` public, `-` private, `#` protected, `*` abstract, underlined = static. Hollow triangles show inheritance/realization, a hollow diamond aggregation, a filled diamond composition.
