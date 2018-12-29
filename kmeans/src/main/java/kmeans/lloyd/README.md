# Lloyd's KMeans Algorithm

> Lloyd's Algorithm is a clustering algorithm that segments data points into clusters based on their distance.

## Pruning techniques:

None

## Variables:

| Type           | Name | Purpose                                                        |
| -------------- | ---- | -------------------------------------------------------------- |
| `int`          | `D`  | number of dimensions                                           |
| `int`          | `K`  | number of clusters (and cluster centers)                       |
| `int`          | `N`  | number of data points                                          |
| `int[N]`       | `a`  | for each data point the index of the cluster it is assigned to |
| `double[K][D]` | `c`  | cluster centers                                                |
| `double[K][D]` | `c'` | for each cluster the vector sum of all its points              |
| `int[K]`       | `q`  | for each cluster the number of its points                      |
| `double[N][D]` | `x`  | data points                                                    |

## Pseudo-code:

```
function lloyd(x, c):
  for n=1 to N do
    a' <- a[n]                                    # store current value for later
    a[n] <- argmin_k d(x[n], c[k])                # compute index of exact closest center
    if a' != a[n] then                            # when the closest cluster index hasn't changed
      q[a[n]] <- q[a[n]] + 1                      # update cluster size
      for d=1 to D do                             # update cluster sum for each dimension
        c'[a[n]][d] <- c'[a[n]][d] + x[n][d]      # update cluster sum for each dimension

  while not converged do
    # compute the nearest cluster for each point
    for n=1 to N do
      a' <- a[n]                                    # store current value for later
      a[n] <- argmin_k d(x[n], c[k])                # compute index of exact closest center
      if a' != a[n] then                            # when the closest cluster index hasn't changed
        q[a'] <- q[a'] - 1                          # update cluster size
        q[a[n]] <- q[a[n]] + 1                      # update cluster size
        for d=1 to D do                             # update cluster sum for each dimension
          c'[a'][d] <- c'[a'][d] - x[n][d]          # update cluster sum for each dimension
          c'[a[n]][d] <- c'[a[n]][d] + x[n][d]      # update cluster sum for each dimension

    # assign each cluster center to the average of its points
    for k=1 to K do
      for d=1 to D do
        c[k][d] <- c'[k][d]/q[k]                    # cluster sum divided by cluster size
```

## Time & Space Complexity

| Initialization time | Time per iteration | Memory             |
| ------------------- | ------------------ | ------------------ |
| -                   | `NKD + KD`         | `N + ND + K + 2KD` |

### Initialization Time

| Initialization Time | Why                                                   | Overhead (compared to Lloyd) |
| ------------------- | ----------------------------------------------------- | ---------------------------- |
| `NKD`               | loop that computes the nearest cluster for each point | ❌                           |

### Time Per Iteration

| Time Per Iteration | Why                                                         | Overhead (compared to Lloyd) |
| ------------------ | ----------------------------------------------------------- | ---------------------------- |
| `NKD`              | loop that computes the nearest cluster for each point       | ❌                           |
| `KD`               | loop that assigns each cluster to the average of its points | ❌                           |

Total Time Per Iteration: `NKD + KD = O(NKD)`\
Total Time Per Iteration Overhead: `0`

### Memory

| Memory | Name | Overhead (compared to Lloyd) |
| ------ | ---- | ---------------------------- |
| `N`    | `a`  | ❌                           |
| `KD`   | `c`  | ❌                           |
| `KD`   | `c'` | ❌                           |
| `K`    | `q`  | ❌                           |
| `ND`   | `x`  | ❌                           |

Total Memory: `N + ND + 2KD + K + = O(ND)`\
Total Memory Overhead: `0`
