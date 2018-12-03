# Elkan's KMeans Algorithm

> Lloyd's Algorithm is a clustering algorithm that segments data points into clusters based on their distance.

## Pruning techniques:

<!-- TODO: -->

## Variables:

<!-- TODO: adjust for elkan -->

| Type           | Name | Purpose                                                                                                   |
| -------------- | ---- | --------------------------------------------------------------------------------------------------------- |
| `int`          | `D`  | number of dimensions                                                                                      |
| `int`          | `K`  | number of clusters (and cluster centers)                                                                  |
| `int`          | `N`  | number of data points                                                                                     |
| `int[N]`       | `a`  | for each data point the index of the cluster it is assigned to                                            |
| `double[K][D]` | `c`  | cluster centers                                                                                           |
| `double[K][D]` | `c'` | for each cluster the vector sum of all its points                                                         |
| `double[K][D]` | `i'` | for each cluster the distance to all the other clusters                                                   |
| `double[N][K]` | `l`  | for each data point and each cluster a lower bound on the distance between the data point and the cluster |
| `int[K]`       | `q`  | for each cluster the number of its points                                                                 |
| `double[K]`    | `s`  | for each cluster center the distance to its closest other center                                          |
| `double[N]`    | `u`  | for each point an upper bound on the distance to its closest center                                       |
| `double[N][D]` | `x`  | data points                                                                                               |

## Pseudo-code:

<!-- TODO: adjust for elkan -->

```
function elkan(x, c):
  while not converged do
    # compute the nearest cluster center for each cluster center
    for k=1 to K do
      s[k] <- min_(k'!=k) d(c[k], c[k'])
      for k'=1 to K do
        i[k'][k] d(c[k'], c[k])

    # compute the nearest cluster for each point
    for n=1 to N do
      m <- s[a[n]]/2
      if u[n] > m then
        a' = a[n]
        for k=1 to K do
          if k!= a[n] && u[n] > l[n][k] && u[n] > 0.5 * i[a[n]][k] then
            if r[n] then
              f <- d(x[n], c[a[n]])
              r[n] <- false
            else
              f <- u[n]

            if f > l[n][k] || f > 0.5 * i[a[n]][k] then
              f' <- d(x[n],c[k])
              if f' < f then
                a[n] = k
                u[n] = f
        if a' != a[n] then                            # when the closest cluster index hasn't changed
          q[a'] <- q[a'] - 1                          # update cluster size
          q[a[n]] <- q[a[n]] + 1                      # update cluster size
          for d=1 to D do                             # update cluster sum for each dimension
            c'[a'][d] <- c'[a'][d] - x[n][d]          # update cluster sum for each dimension
            c'[a[n]][d] <- c'[a[n]][d] + x[n][d]      # update cluster sum for each dimension

    # assign each cluster center to the average of its points
    for k=1 to K do
      c* <- c[k]                                    # store old center for later
      for d=1 to D do
        c[k][d] <- c'[k][d]/q[k]                    # cluster sum divided by cluster size
      p[k] <- d(c*,c[k])                            # store the distance that the center has moved
```

## Time & Space Complexity Overhead (compared to Lloyd)

| Initialization time | Time per iteration | Memory   |
| ------------------- | ------------------ | -------- |
| NDK +K^2            | DK^2               | NK + K^2 |

## Exact Time & Space Complexity

### Initialization Time

### Time per Iteration

### Memory

| Memory | Name |
| ------ | ---- |
| `N`    | `a`  |
| `KD`   | `c`  |
| `KD`   | `c'` |
| `NK`   | `l`  |
| `K`    | `q`  |
| `K`    | `p`  |
| `K`    | `s`  |
| `N`    | `u`  |
| `N`    | `x`  |

Total: 3N +
