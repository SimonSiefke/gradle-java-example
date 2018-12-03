# Hamerly's KMeans Algorithm

> Hamerly's Algorithm is a clustering algorithm that segments data points into clusters based on their distance.

## Pruning techniques:

<!-- TODO: svg -->

- skip the assignment loop when the upper bound (maximal distance to assigned center) from the last or the current iteration is smaller than the lower bound (minimal distance to second closest center), which means that the currently assigned center stays the same.
- skip the assignment loop when the upper bound (maximal distance to assigned center) from the last or the current iteration is smaller than half of the distance between the assigned center and its nearest neighbor.

## Variables:

| Type           | Name | Purpose                                                                        |
| -------------- | ---- | ------------------------------------------------------------------------------ |
| `int`          | `D`  | number of dimensions                                                           |
| `int`          | `K`  | number of clusters (and cluster centers)                                       |
| `int`          | `N`  | number of data points                                                          |
| `int[N]`       | `a`  | for each data point the index of the cluster it is assigned to                 |
| `double[K][D]` | `c`  | cluster centers                                                                |
| `double[K][D]` | `c'` | for each cluster the vector sum of all its points                              |
| `double[N]`    | `l`  | for each data point a lower bound on the distance to its second closest center |
| `int[K]`       | `q`  | for each cluster the number of its points                                      |
| `double[K]`    | `p`  | for each cluster center the distance that it last moved                        |
| `double[K]`    | `s`  | for each cluster center the distance to its closest other center               |
| `double[N]`    | `u`  | for each point an upper bound on the distance to its closest center            |
| `double[N][D]` | `x`  | data points                                                                    |

## Pseudo-code:

<!-- TODO: update bounds code -->

```
function hamerly(x, c):
  while not converged do
    # compute the nearest cluster center for each cluster center
    for k=1 to K do
      s[k] <- min_(k'!=k) d(c[k], c[k'])

    # compute the nearest cluster for each point
    for n=1 to N do
      m <- max(s[a[n]]/2, l[n])
      if u[n] > m then                              # first bound test
        u[n] <- d(x[n], c[a[n]])                    # tighten upper bound
        if u[n] > m then                            # second bound test
          a' <- a[n]                                # store current value for later
          a[n] <- argmin_k d(x[n], c[k])            # compute index of exact closest center
          u[n] <- d(x[n], c[a[n]])                  # upper bound is now the exact distance to closest center
          l[n] <- min_k!=a[n] d(x[n],c[k])          # lower bound is now the exact distance to the second closest center
          if a' != a[n] then                        # when the closest cluster index hasn't changed
            q[a'] <- q[a'] - 1                      # update cluster size
            q[a[n]] <- q[a[n]] + 1                  # update cluster size
            for d=1 to D do                         # update cluster sum for each dimension
              c'[a'][d] <- c'[a'][d] - x[n][d]      # update cluster sum for each dimension
              c'[a[n]][d] <- c'[a[n]][d] + x[n][d]  # update cluster sum for each dimension

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
| `N`    | `a`  | ❌                           |
| `KD`   | `c`  | ❌                           |
| `KD`   | `c'` | ❌                           |
| `N`    | `l`  | ✅                           |
| `K`    | `q`  | ❌                           |
| `K`    | `p`  | ✅                           |
| `K`    | `s`  | ✅                           |
| `N`    | `u`  | ✅                           |
| `ND`   | `x`  | ❌                           |

Total Memory: 3N + ND + 3K + 2KD = O(ND)

Total Memory Overhead: 2N + 2K
