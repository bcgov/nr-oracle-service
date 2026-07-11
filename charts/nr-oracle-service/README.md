# nr-oracle-service

## Configuration

The following table lists the configurable parameters and their default values.

| Parameter                               | Description                                              | Default |
|-----------------------------------------|------------------------------------------------------------|---------|
| `app.envs.DB_HOST`                      | Oracle DB hostname                                          |         |
| `app.envs.DB_NAME`                      | Oracle DB / service name                                     |         |
| `app.envs.DB_PASSWORD`                  | Oracle DB password                                           |         |
| `app.envs.DB_PORT`                      | Oracle DB port                                                |         |
| `app.envs.DB_USER`                      | Oracle DB username                                            |         |
| `app.envs.HTTP_PORT`                    | Port the API listens on                                       | 3000    |
| `app.envs.POOL_IDLE_TIMEOUT`            | JDBC pool idle-removal interval (ms)                          | 60000   |
| `app.envs.POOL_INITIAL_SIZE`            | Initial JDBC pool size                                        | 1       |
| `app.envs.POOL_LEAK_DETECTION_INTERVAL` | JDBC pool connection leak detection interval (ms)             | 45000   |
| `app.envs.POOL_MAX_LIFETIME`            | Max lifetime of a pooled JDBC connection (ms)                 | 180000  |
| `app.envs.POOL_MAX_SIZE`                | Max JDBC pool size                                             | 1       |
| `app.envs.POOL_MIN_SIZE`                | Min JDBC pool size                                             | 1       |
| `app.envs.ACCESS_LOG_ENABLED`           | Enable HTTP access logging                                    | false   |

`certSecret` (trust-store password) and `apiKey` (`X-API-Key` value) are generated
automatically by the chart on first install and stored in a Secret — they are not set
via `values.yaml`.

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.
Alternatively, a YAML file that specifies the values for the above parameters can be provided while installing the chart. For example,
```
$ helm install --name chart-name -f values.yaml .
```
> **Tip**: You can use the default [values.yaml](values.yaml)
