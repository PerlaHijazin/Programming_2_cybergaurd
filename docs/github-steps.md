# GitHub and submission steps

These actions must be completed in your own GitHub account. This package does not claim they have already happened.

1. Create a repository named `CyberGuard`, with `main` as the default branch. Follow your instructor's public/private requirement. Add the instructor as a collaborator before development begins, as the assignment requests. If development has already begun, explain the timing honestly.
2. If using GitHub Desktop, create/add the extracted `CyberGuard` folder as a local repository. The folder containing `pom.xml` is the repository root.
3. Review the files before publishing. Do not include `target`, `.idea`, `*.iml`, `data`, passwords, or API keys. `.gitignore` covers the normal local outputs.
4. Create GitHub issues from the user stories. Suggested issues: review the domain model; implement incident creation; enforce status workflow; add sorting/filtering; implement persistence; add tests; review UML and README.
5. Make a meaningful initial commit for the state you actually upload. Do not invent past commits or describe already completed work as still pending.
6. For further changes, create a feature branch in GitHub Desktop, for example `feature/improve-validation`. Make and test a real change, commit it, publish the branch, open a pull request, and merge it into main. Repeat for genuine features or fixes. A ZIP alone cannot supply visible branch/merge history.
7. Build and run the project from a fresh clone. Run `mvn clean test` and `mvn package`, or use IntelliJ's Maven Lifecycle actions. Verify save and restore manually.
8. Review UML, contributor name, license, and documentation. Check that the instructor can access the repository.
9. When the final version is ready on main, use GitHub's Releases page to draft a release. Create tag `v1.0.0` targeting the verified final commit and paste the reviewed release notes from `release-notes.md`.
10. Publish the release and submit the repository URL by the assignment deadline, 29 November 2026.

For a private repository, the link alone does not grant instructor access. Verify the invitation has been accepted.
