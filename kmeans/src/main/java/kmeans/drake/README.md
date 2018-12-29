# Drake's KMeans Algorithm

> Drake's Algorithm is a clustering algorithm that segments data points into clusters based on their distance.

## Pruning techniques:

<!-- TODO: svg -->

## Variables:

| Type           | Name | Purpose                                                             |
| -------------- | ---- | ------------------------------------------------------------------- |
| `int`          | `D`  | number of dimensions                                                |
| `int`          | `K`  | number of clusters (and cluster centers)                            |
| `int`          | `N`  | number of data points                                               |
| `int[N]`       | `a`  | for each data point the index of the cluster it is assigned to      |
| `double[K][D]` | `c`  | cluster centers                                                     |
| `double[K][D]` | `c'` | for each cluster the vector sum of all its points                   |
| `int[K]`       | `q`  | for each cluster the number of its points                           |
| `double[K]`    | `p`  | for each cluster center the distance that it last moved             |
| `double[K]`    | `s`  | for each cluster center the distance to its closest other center    |
| `double[N]`    | `u`  | for each point an upper bound on the distance to its closest center |
| `double[N][D]` | `x`  | data points                                                         |

## Pseudo-code:

<!-- TODO: update bounds code -->

```
function hamerly(x, c):

  while not converged do

    # assign each cluster center to the average of its points
    for k=1 to K do
      c* <- c[k]                                    # store old center for later
      for d=1 to D do
        c[k][d] <- c'[k][d]/q[k]                    # cluster sum divided by cluster size
      p[k] <- d(c*,c[k])                            # store the distance that the center has moved
```

## Time & Space Complexity

| Initialization time | Time per iteration | Memory   |
| ------------------- | ------------------ | -------- |
| NDK + K^2           | DK^2               | NK + K^2 |

## Exact Time & Space Complexity

### Initialization Time

### Memory

| Memory | Name | Overhead (compared to Lloyd) |
| ------ | ---- | ---------------------------- |


Total Memory: `\ Total Memory Overhead:`
