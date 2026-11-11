# Kubernetes deployment

The manifests use the development password in `postgres-secret.yaml`. Change it
before deploying outside a local development cluster.

Build the container images from the project root:

```sh
docker build -t connect4-backend:latest ./backend
docker build -t connect4-frontend:latest ./frontend
```

Apply the manifests and wait for the workloads:

```sh
kubectl apply -f k8s/
kubectl rollout status deployment/postgres
kubectl rollout status deployment/backend
kubectl rollout status deployment/frontend
```

The frontend is exposed through a ClusterIP service. Forward it to localhost:

```sh
kubectl port-forward service/frontend 8081:80
```

Open `http://localhost:8081`. The frontend container forwards `/api` requests
to the in-cluster `backend` service, which connects to `postgres`.

The PostgreSQL claim uses the cluster's default StorageClass. Configure a
StorageClass or update `postgres-pvc.yaml` if the cluster has no default.
