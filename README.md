# Connect Four

A Connect Four web application with a React/TypeScript frontend, Spring Boot
backend, and PostgreSQL database.

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (Windows or
  macOS), or Docker Engine with the Compose v2 plugin (Linux).
- Docker Compose v2, available as `docker compose`.
- Ports `8081` and `5433` available on your machine by default.

The images are built from the included Dockerfiles, so Java, Maven, Node.js, and
PostgreSQL do not need to be installed locally.

## Run the entire application

Open a terminal in the project root and run:

```sh
docker compose up --build
```

Compose builds and starts the frontend, backend, and PostgreSQL. The backend
waits for the database health check before starting. When the containers are
ready, open [http://localhost:8081](http://localhost:8081).

The frontend container serves the app and forwards `/api` requests to the
backend. PostgreSQL data is stored in the `postgres_data` Docker volume and
persists when containers are stopped or recreated.

To run in the background instead:

```sh
docker compose up --build -d
```

Stop the application with:

```sh
docker compose down
```

To also delete the persisted database data:

```sh
docker compose down --volumes
```

## Optional: run with Kubernetes

You can run the app with Kubernetes instead of Docker Compose. You’ll need a
local or remote Kubernetes cluster, `kubectl` configured to use it, and a
default StorageClass for the PostgreSQL volume.

Build the images from the project root:

```sh
docker build -t connect4-backend:latest ./backend
docker build -t connect4-frontend:latest ./frontend
```

Make sure your cluster can access these images. For local clusters, you may
need to load them into the cluster or build them using its container runtime.
Then apply the manifests and wait for the application:

```sh
kubectl apply -f k8s/
kubectl rollout status deployment/postgres
kubectl rollout status deployment/backend
kubectl rollout status deployment/frontend
```

Forward the frontend service to your machine and open
[http://localhost:8081](http://localhost:8081):

```sh
kubectl port-forward service/frontend 8081:80
```

Stop the port forward with `Ctrl+C`. To remove the Kubernetes deployment:

```sh
kubectl delete -f k8s/
```

The Kubernetes manifests use a development database password. Change
`k8s/postgres-secret.yaml` before using them outside a local development
cluster. For more details, see [k8s/README.md](k8s/README.md).

## Configuration

Defaults are configured in `docker-compose.yml`:

| Variable | Default | Purpose |
| --- | --- | --- |
| `FRONTEND_PORT` | `8081` | Host port for the web app |
| `POSTGRES_PORT` | `5433` | Host port for PostgreSQL |
| `POSTGRES_PASSWORD` | `connect4` | PostgreSQL password used by the database and backend |

Set any of these in your shell before running Compose to override the defaults.
These credentials are for local development; change them before using this
setup in a shared or production environment.

## Useful commands

```sh
docker compose ps
docker compose logs -f
docker compose logs -f backend
```
