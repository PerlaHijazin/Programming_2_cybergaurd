# Design decisions

- Use familiar classes, constructors, private fields, getters, loops, `Scanner`, and `ArrayList`, following the supplied class examples.
- Use an abstract parent and two subclasses, like the Animal/Dog/Cat example. Their advice is different at runtime.
- Use Factory Method, like the character-creation practice. Factories own concrete incident construction.
- Keep `Severity` and `Status` as enums rather than arbitrary strings, so invalid values cannot enter the model.
- Model an affected asset as its own object. Each incident has one asset; a manager has zero or more incidents. Asset objects are immutable through their public API and may be shared by several incident objects.
- Give `Incident` a natural ordering by severity, like the Comparable exercise. A sorted copy protects the manager's list order.
- Make status transitions a domain rule enforced in `Incident`, not just in menu code.
- Use explicit, unique IDs and calculate the next ID from loaded records so restarting does not reuse an existing ID.
- Store simple text, not Java serialization, to keep records inspectable and avoid tying the data format to compiled class internals.
- Validate a complete load before returning it. Stop startup on corrupt files. Do not silently overwrite unreadable records.
- Separate domain, service, persistence, and UI packages, as the assignment requests.

## Known limitations

See README: local single-user application, two incident types, one asset per record, sequential statuses, no authentication or concurrent access. Text fields cannot contain tabs or line breaks. A controlled exit saves changes; a forced shutdown may not. A new incident type requires updating type-selection code as well as adding a subclass and factory.

## UML

Open `uml.svg` for the viewable class diagram. Edit `uml.mmd` for the Mermaid source. `+` means public and `-` means private. Hollow triangle arrows show inheritance/realization; diamonds show aggregation. Multiplicities describe stored relationships, not local variables.
