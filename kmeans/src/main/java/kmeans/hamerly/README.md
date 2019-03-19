# Hamerly's K-Means Algorithm

Hamerly's Algorithm tries to optimize Lloyd's Algorithm by using pruning techniques to skip distance calculations and increase performance, while yielding the same results as Lloyd's Algorithm in the end and also after each iteration. It also needs the same number of iterations to converge.

1. The first pruning technique uses N upper bounds (one for each data point) and N lower bounds (one for each data point). For each point the upper bound is the maximal distance between itself and its currently assigned center. The lower bound is the minimal distance between the data point and its second nearest center. If the distance between a data point and its second closest cluster (lower bound) is less than the distance between the data point and its assigned center (upper bound) we know that the point keeps its assigned center and we don't need to compute the distance to any other center.
2. The second pruning technique uses the hyperplane distance between two cluster centers: Let x be a data point, c its assigned center and u the upper bound from x to c. If the distance between c and each of the other cluster centers is at least two times u then we know that none of the other cluster centers can possibly be closer to x than c. Thus, the point keeps its assigned center and we don't need to compute the distance to any other center.

## Variables:

| Type           | Memory | Name | Purpose                                                                        |
| -------------- | ------ | ---- | ------------------------------------------------------------------------------ |
| `int`          |        | `D`  | number of dimensions                                                           |
| `int`          |        | `K`  | number of clusters (and cluster centers)                                       |
| `int`          |        | `N`  | number of data points                                                          |
| `int[N]`       | `N`    | `a`  | for each data point the index of the cluster it is assigned to                 |
| `double[K][D]` | `KD`   | `c`  | cluster centers                                                                |
| `double[K][D]` | `KD`   | `c'` | for each cluster the vector sum of all its points                              |
| `double[N]`    | `N`    | `l`  | for each data point a lower bound on the distance to its second closest center |
| `int[K]`       | `K`    | `q`  | for each cluster the number of its points                                      |
| `double[K]`    | `K`    | `p`  | for each cluster center the distance that it last moved                        |
| `double[K]`    | `K`    | `s`  | for each cluster center the distance to its closest other center               |
| `double[N]`    | `N`    | `u`  | for each point an upper bound on the distance to its closest center            |
| `double[N][D]` | `ND`   | `x`  | data points                                                                    |

Total Additional Memory: `3N + KD + 3K`

## Pseudo-code (simple):

```
function hamerly(x, c):
  for n=0 to N-1 do
    u[n] <- min_k d(x[n], c[k])                              # compute exact distance to closest center
    l[n] <- argmin_(k!=a[n]) d(x[n], c[k])                   # compute index of second closest center
    initialAssignPointToCluster(n, argmin_k d(x[n], c[k]))   # assign to index of exact closest center
  moveCenters()
  while not converged do
    for k=0 to K-1 do
      s[k] <- min_(k'!=k) d(c[k], c[k'])                     # compute the nearest cluster center for each cluster center
    for n=0 to N-1 do                                        # compute the nearest cluster for each point
      m <- max(s[a[n]]/2, l[n])
      if u[n] > m then                                       # first bound test
        u[n] <- d(x[n], c[a[n]])                             # tighten upper bound to see if we can prune with the exact distance to closest center
        if u[n] > m then                                     # second bound test
          a[n] <- argmin_k d(x[n], c[k])
          u[n] <- d(x[n], c[a[n]])                           # upper bound is now the exact distance to closest center
          l[n] <- min_k!=a[n] d(x[n],c[k])                   # lower bound is now the exact distance to the second closest center
          assignPointToCluster(n, argmin_k d(x[n], c[k]))    # assign to index of exact closest center
    moveCenters()
    r  <- argmax_k p                                         # index of the center that has moved the most
    r' <- argmax_k!=r p                                      # index of the center that has moved the second most
    for n=0 to N-1 do
      u[n] <- u[n] + p[a[n]]                                 # increase upper bound by the distance that the assigned center has moved
      if r=a[n] then                                         # when its the center that has moved the most
        l[n] <- l[n] - p[r']                                 # decrease by the second most distance moved
      else                                                   # when its not the center that has moved the most
        l[n] <- l[n] - p[r]                                  # decrease by the most distance moved
```

## Pseudo-code (extended):

```
function hamerly(x, c):
  for n=0 to N-1 do
    a[n] <- argmin_k d(x[n], c[k])                  # compute index of closest center
    u[n] <- min_k d(x[n], c[k])                     # compute exact distance to closest center
    l[n] <- argmin_(k!=a[n]) d(x[n], c[k])          # compute index of second closest center
    q[a[n]] <- q[a[n]] + 1                          # update cluster size
    for d=0 to D-1 do                               # update cluster sum for each dimension
      c'[a[n]][d] <- c'[a[n]][d] + x[n][d]          # update cluster sum for each dimension
  while not converged do
    for k=0 to K-1 do
      s[k] <- min_(k'!=k) d(c[k], c[k'])            # compute the nearest cluster center for each cluster center
    for n=0 to N-1 do                               # compute the nearest cluster for each point
      m <- max(s[a[n]]/2, l[n])
      if u[n] > m then                              # first bound test
        u[n] <- d(x[n], c[a[n]])                    # tighten upper bound to see if we can prune with the exact distance to closest center
        if u[n] > m then                            # second bound test
          a' <- a[n]                                # store current value for later
          a[n] <- argmin_k d(x[n], c[k])            # compute index of exact closest center
          u[n] <- d(x[n], c[a[n]])                  # upper bound is now the exact distance to closest center
          l[n] <- min_k!=a[n] d(x[n],c[k])          # lower bound is now the exact distance to the second closest center
          if a' != a[n] then                        # when the closest cluster index hasn't changed
            q[a'] <- q[a'] - 1                      # update cluster size
            q[a[n]] <- q[a[n]] + 1                  # update cluster size
            for d=0 to D-1 do                       # update cluster sum for each dimension
              c'[a'][d] <- c'[a'][d] - x[n][d]      # update cluster sum for each dimension
              c'[a[n]][d] <- c'[a[n]][d] + x[n][d]  # update cluster sum for each dimension
    for k=0 to K-1 do                               # assign each cluster center to the average of its points
      c* <- c[k]                                    # store old center for later
      for d=0 to D-1 do
        c[k][d] <- c'[k][d]/q[k]                    # cluster sum divided by cluster size
      p[k] <- d(c*,c[k])                            # store the distance that the center has moved
    r  <- argmax_k p                                # index of the center that has moved the most
    r' <- argmax_k!=r p                             # index of the center that has moved the second most
    for n=0 to N-1 do
      u[n] <- u[n] + p[a[n]]                        # increase upper bound by the distance that the assigned center has moved
      if r=a[i] then
        l[n] <- l[n] - p[r']                        # decrease by the second most distance moved
      else
        l[n] <- l[n] - p[r]                         # decrease by the most distance moved
```
