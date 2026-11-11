# Development Notes

## Main Decisions

- **Java and Spring Boot:** Used for backend game logic, move validation, and REST API endpoints.
- **React and TypeScript:** Used for a responsive frontend that communicates with the backend.
- **PostgreSQL:** Persists game state between requests and application restarts.
- **Docker Compose:** Allows the entire application to run locally with one command.
- **Kubernetes:** Provides an alternative container deployment using Deployments, Services, and persistent storage.

## Trade-offs

- **Simple board representation:** Stored the 6×7 board as a 42-character string for simplicity.
- **Local multiplayer only:** Prioritized a complete two-player game over online multiplayer or user accounts.
- **Minimal deployment configuration:** Focused on a functional local Kubernetes setup rather than production infrastructure.
- **Core functionality first:** Prioritized gameplay, reliability, and ease of setup over additional features.

## What I Would Do With More Time

- Add more automated API and integration tests.
- Improve HTTP error handling for invalid moves.
- Add Kubernetes readiness checks to prevent temporary startup errors.
- Implement move history and online multiplayer.

## Time Breakdown

| Task | Time Spent |
|---|---|
| Backend and game logic | ~1 hour |
| Frontend and API integration | ~1 hour |
| Docker and PostgreSQL | ~40 mins |
| Kubernetes | ~20 mins |
| Testing and debugging | ~45 mins |
| Documentation | ~15 mins |
| **Total** | **~4 hours** |

## AI Assistance

GitHub Copilot assisted with portions of the frontend, Docker, Kubernetes configuration, tests, and documentation.

Agent transcript included separately.