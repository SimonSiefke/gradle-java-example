# Lloyd's KMeans Algorithm

Lloyd's Algorithm is a clustering algorithm that segments data points into clusters based on their distance.

## Variables & Memory:

| Type           | Memory | Name | Purpose                                                        |
| -------------- | ------ | ---- | -------------------------------------------------------------- |
| `int`          |        | `D`  | number of dimensions                                           |
| `int`          |        | `K`  | number of clusters (and cluster centers)                       |
| `int`          |        | `N`  | number of data points                                          |
| `int[N]`       | `N`    | `a`  | for each data point the index of the cluster it is assigned to |
| `double[K][D]` | `KD`   | `c`  | cluster centers                                                |
| `double[K][D]` | `KD`   | `c'` | for each cluster the vector sum of all its points              |
| `int[K]`       | `K`    | `q`  | for each cluster the number of its points                      |
| `double[N][D]` | `ND`   | `x`  | data points                                                    |

Total Additional Memory: `N + KD + K`

## Pseudo-code (simple):

```
function lloyd(x, c):
  for n=0 to N-1 do
    initialAssignPointToCluster(n, argmin_k d(x[n], c[k]))   # assign to index of exact closest center

  moveCenters()

  while not converged do
    for n=0 to N-1 do
      assignPointToCluster(n, argmin_k d(x[n], c[k]))        # assign to index of exact closest center
      moveCenters()
```

## Pseudo-code (extended):

```
function lloyd(x, c):
  for n=0 to N-1 do
    a[n] <- argmin_k d(x[n], c[k])                           # compute index of the closest center
    q[a[n]] <- q[a[n]] + 1                                   # update cluster size
    for d=0 to D-1 do                                        # update cluster sum for each dimension
      c'[a[n]][d] <- c'[a[n]][d] + x[n][d]                   # update cluster sum for each dimension

  while not converged do
    for n=0 to N-1 do                                        # compute the nearest cluster for each point
      a' <- a[n]                                             # store current value for later
      a[n] <- argmin_k d(x[n], c[k])                         # compute index of exact closest center
      if a' != a[n] then                                     # when the closest cluster index hasn't changed
        q[a'] <- q[a'] - 1                                   # update cluster size
        q[a[n]] <- q[a[n]] + 1                               # update cluster size
        for d=0 to D-1 do                                    # update cluster sum for each dimension
          c'[a'][d] <- c'[a'][d] - x[n][d]                   # update cluster sum for each dimension
          c'[a[n]][d] <- c'[a[n]][d] + x[n][d]               # update cluster sum for each dimension

    for k=0 to K-1 do                                        # assign each cluster center to the average of its points
      for d=0 to D-1 do
        c[k][d] <- c'[k][d]/q[k]                             # cluster sum divided by cluster size
```
