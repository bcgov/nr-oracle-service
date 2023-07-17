# nr-oracle-service

## Configuration

The following table lists the configurable parameters and their default values.

| Parameter | Description | Default |
|  ---  |  ---  |  ---  |
| `app.envs.DB_HOST` |   |   |
| `app.envs.DB_NAME` |   |   |
| `app.envs.DB_PASSWORD` |   |   |
| `app.envs.DB_PORT` |   |   |
| `app.envs.DB_SECRET` |   |   |
| `app.envs.DB_USER` |   |   |
| `app.envs.HTTP_PORT` |   | 3000 |
| `app.envs.POOL_IDLE_TIMEOUT` |   | 60000 |
| `app.envs.POOL_INITIAL_SIZE` |   | 2 |
| `app.envs.POOL_LEAK_DETECTION_INTERVAL` |   | 300000 |
| `app.envs.POOL_MAX_LIFETIME` |   | 180000 |
| `app.envs.POOL_MAX_SIZE` |   | 2 |
| `app.envs.POOL_MIN_SIZE` |   | 2 |
| `app.image` | The container image to use. | ompra/nr-oracle-service:1.0.0-SNAPSHOT |
| `app.ports.http` | The http port to use for the probe. | 3000 |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.
Alternatively, a YAML file that specifies the values for the above parameters can be provided while installing the chart. For example,
```
$ helm install --name chart-name -f values.yaml .
```
> **Tip**: You can use the default [values.yaml](values.yaml)
