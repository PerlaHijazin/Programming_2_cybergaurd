# User stories and acceptance criteria

1. As a security analyst, I want to record a phishing or malware incident so that I can track what happened.
   - A valid record has an automatically assigned positive ID, title, description, asset, severity, and initial OPEN status.
   - Empty required text and invalid severity are rejected without adding a record.
2. As an analyst, I want to list incidents in priority order so I can review the most serious first.
   - CRITICAL comes before HIGH, MEDIUM, and LOW; equal severity is ordered by ID.
   - Each record includes advice specific to its incident type.
3. As an analyst, I want to revise severity when I learn more about the incident.
   - Existing IDs can be updated; unknown IDs display a clear error.
4. As an analyst, I want to track investigation progress.
   - OPEN can become INVESTIGATING, which can become RESOLVED.
   - Skipping investigation or reopening a resolved incident is rejected.
5. As an analyst, I want to filter records by status so I can focus on active work.
   - The result contains only records with the selected status and stays sorted by severity.
6. As an analyst, I want records to survive restarts.
   - Saving and restarting preserves all fields and concrete types.
   - Missing data files start empty; corrupt files stop startup rather than silently dropping records.

These are the local backlog and acceptance criteria. Create actual GitHub issues for visible repository backlog evidence; this document alone is not a GitHub issue history.
