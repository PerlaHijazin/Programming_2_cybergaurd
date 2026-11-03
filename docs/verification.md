# Verification - 2 November 2026

- Production source compiled with JDK 27 javac targeting Java 17 (`--release 17`).
- Six JUnit tests passed, with zero failures, errors, or skips. Verified with JUnit Platform and again through Maven Surefire.
- Maven `surefire:test jar:jar` succeeded using the compiled classes and test classes.
- The Maven-generated executable JAR was run to create a malware incident, save it, restart, and confirm restoration.
- Additional console checks covered both incident types, severity updates, status filtering, rejected status skipping, invalid numeric input, incomplete input/EOF, and corrupt-file protection.
- The class diagram was rendered and visually reviewed.

## Local environment limitation

The full `mvn clean test package` lifecycle could not complete in this restricted Windows environment: javac could not resolve some classpath entries and reported AccessDeniedException while closing a dependency JAR. Production compilation succeeded with explicit source files. Test compilation succeeded using the Eclipse compiler in an isolated working directory; the unmodified tests then passed in both JUnit Platform and Maven Surefire. The workaround tools and downloaded dependencies are not part of the delivered project.

The project retains standard Maven compiler configuration. Run the documented `mvn clean test` and `mvn package` commands in IntelliJ or a normal local terminal before submission. A successful clean-checkout build is not claimed by this verification report.
