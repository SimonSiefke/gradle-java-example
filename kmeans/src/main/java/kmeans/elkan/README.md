# Elkan's KMeans Algorithm

> Elkan's Algorithm is a clustering algorithm that segments data points into clusters based on their distance. It uses triangle inequality to skip some distance calculation which makes it more efficient than Lloyd's Algorithm.

## Pruning techniques:

Using triangle equality and hyperplane distance and distance to second closest center

<!-- TODO: -->

## Variables:

| Type           | Memory | Name | Purpose                                                                                                   |
| -------------- | ------ | ---- | --------------------------------------------------------------------------------------------------------- |
| `int`          |        | `D`  | number of dimensions                                                                                      |
| `int`          |        | `K`  | number of clusters (and cluster centers)                                                                  |
| `int`          |        | `N`  | number of data points                                                                                     |
| `int[N]`       | `N`    | `a`  | for each data point the index of the cluster it is assigned to                                            |
| `double[K][D]` | `KD`   | `c`  | cluster centers                                                                                           |
| `double[K][D]` | `KD`   | `c'` | for each cluster the vector sum of all its points                                                         |
| `double[K][K]` | `K^2`  | `i'` | for each cluster the distance to all the other clusters                                                   |
| `double[N][K]` | `NK`   | `l`  | for each data point and each cluster a lower bound on the distance between the data point and the cluster |
| `int[K]`       | `K`    | `q`  | for each cluster the number of its points                                                                 |
| `double[K]`    | `K`    | `s`  | for each cluster center the distance to its closest other center                                          |
| `double[N]`    | `N`    | `u`  | for each point an upper bound on the distance to its closest center                                       |
| `double[N][D]` | `ND`   | `x`  | data points                                                                                               |

Total Additional Memory: `2N + NK + K^2 + 2K + KD`

## Pseudo-code:

```
function elkan(x, c):
  for n=0 to N-1 do
    a[n] <- argmin_k d(x[n], c[k])                          # compute index of the closest center
    u[n] <- min d(x[n], c[k])                               # compute distance to the closest center
    for k=0 to K-1 do
      l[n][k] <- d(x[n],c[k])                               # compute exact distance to each center
    q[a[n]] <- q[a[n]] + 1                                  # update cluster size
    for d=0 to D-1 do                                       # update cluster sum for each dimension
      c'[a[n]][d] <- c'[a[n]][d] + x[n][d]                  # update cluster sum for each dimension

  while not converged do
    for k=0 to K-1 do
      s[k] <- 0.5 * min_(k'!=k) d(c[k], c[k'])              # compute the nearest cluster center for each cluster center
      for k'=1 to K do
        i[k'][k] <- d(c[k'], c[k])                          # compute the distance to every cluster center for each cluster center

    for n=0 to N-1 do                                       # compute the nearest cluster for each point
      m <- s[a[n]]/2
      if u[n] > m then
        a' = a[n]
        for k=0 to K-1 do
          if k!= a[n] && u[n] > l[n][k] && u[n] > 0.5 * i[a[n]][k] then
            if r[n] then                                    # if the upper bound is inaccurate
              f <- d(x[n], c[a[n]])                         # compute the exact distance to nearest center
              u[n] = f                                      # assign upper bound the exact distance
              r[n] <- false                                 # upper bound is no longer inaccurate
            else
              f <- u[n]                                     # upper bound is the exact distance to nearest center
            if f > l[n][k] || f > 0.5 * i[a[n]][k] then     # try to prune with hyperplane distance and the lower bound
              f' <- d(x[n],c[k])                            # compute distance to new candidate
              if f' < f then	                              # if candidate is closer
                a[n] = k	                                  # assign point to new candidate
                u[n] = f'                                   # update bound
        if a' != a[n] then                                  # when the closest cluster index hasn't changed
          q[a'] <- q[a'] - 1                                # update cluster size
          q[a[n]] <- q[a[n]] + 1                            # update cluster size
          for d=0 to D-1 do                                 # update cluster sum for each dimension
            c'[a'][d] <- c'[a'][d] - x[n][d]                # update cluster sum for each dimension
            c'[a[n]][d] <- c'[a[n]][d] + x[n][d]            # update cluster sum for each dimension

    for k=0 to K-1 do                                       # assign each cluster center to the average of its points
      c* <- c[k]                                            # store old center for later
      for d=0 to D-1 do
        c[k][d] <- c'[k][d]/q[k]                            # cluster sum divided by cluster size
```

## Time Complexity

| Initialization Time | Why                                                   |
| ------------------- | ----------------------------------------------------- |
| `NKD`               | loop that computes the nearest cluster for each point |

| Time Per Iteration | Why                                                         |
| ------------------ | ----------------------------------------------------------- |
| `K^2`              | loop that computes distance between every cluster           |
| `NKD`              | loop that computes the nearest cluster for each point       |
| `KD`               | loop that assigns each cluster to the average of its points |

Total Time Per Iteration: `NKD + K^2 + KD`
