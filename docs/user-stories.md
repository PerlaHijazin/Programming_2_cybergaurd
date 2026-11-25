# User stories and acceptance criteria

1. As a security analyst, I want to record a phishing or malware incident so that I can track what happened.
   - A valid record gets an automatic positive ID, title, description, asset, severity, and starts OPEN.
   - Empty text or an invalid severity is rejected and nothing is added.
2. As an analyst, I want to list incidents in priority order so I can review the most serious first.
   - CRITICAL comes before HIGH, MEDIUM, and LOW; equal severity is ordered by ID.
   - Each record shows advice specific to its incident type.
3. As an analyst, I want to change severity when I learn more about an incident.
   - Existing IDs can be updated; unknown IDs show a clear error.
4. As an analyst, I want to track investigation progress.
   - OPEN → INVESTIGATING → RESOLVED; skipping or going back is rejected.
5. As an analyst, I want to filter incidents by status so I can focus on active work.
   - Only matching incidents are shown, still sorted by severity.
6. As an analyst, I want my incidents to survive restarts.
   - Saving writes `data/incidents.json`; restarting restores all fields and incident types.
   - A missing file starts empty; an invalid file stops startup instead of being overwritten.

Each story is also tracked as a GitHub issue.
