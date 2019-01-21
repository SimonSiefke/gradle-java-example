# Drake's KMeans Algorithm

Drake's Algorithm tries to optimize Lloyd's Algorithm by using pruning techniques to skip distance calculations and increase performance, while yielding the same results as Lloyd's Algorithm in the end and also after each iteration. It also needs the same number of iterations to converge.

1. The pruning technique uses N upper bounds (1 for each data point) and B lower bounds (1<B<K, the value of B changes over time). For each point is the upper bound the distance that the currently assigned center is maximally away. The first (B-1) lower bounds contain the distance the (B+2)-closest center is minimally away (e.g. l[0] is a bound on distance to the second closest center for a data point, l[1] on the distance third closest center). The last lower bound (l[B-1]) is how far every other center (that is not in the bounds l[0]...l[B-2]) minimally is, it is a lower bound for the rest. The lower bounds are always kept in that order, from smallest distance to largest distance. Now we can use this to prune:
   If the second closest center for a point (l[0]) is minimally further away than the closest center is maximally away (upper bound) we know that the point keeps its assigned center and we don't need to compute the distance to any other center.

## Pruning techniques:

<!-- TODO: svg -->

## Variables:

| Type           | Memory | Name | Purpose                                                                         |
| -------------- | ------ | ---- | ------------------------------------------------------------------------------- |
| `int`          |        | `D`  | number of dimensions                                                            |
| `int`          |        | `K`  | number of clusters (and cluster centers)                                        |
| `int`          |        | `N`  | number of data points                                                           |
| `int[N]`       |        | `a`  | for each data point the index of the cluster it is assigned to                  |
| `double[K][D]` | `KD`   | `c`  | cluster centers                                                                 |
| `double[K][D]` | `KD`   | `c'` | for each cluster the vector sum of all its points                               |
| `double[N]`    | `NB`   | `l`  | for each data point B lower bounds on the distance to its second closest center |
| `int[K]`       | `K`    | `q`  | for each cluster the number of its points                                       |
| `double[K]`    | `K`    | `p`  | for each cluster center the distance that it last moved                         |
| `double[K]`    | `K`    | `s`  | for each cluster center the distance to its closest other center                |
| `double[N]`    | `N`    | `u`  | for each point an upper bound on the distance to its closest center             |
| `double[N][D]` | `ND`   | `x`  | data points                                                                     |

Total Additional Memory: `NB + N + KD + 3K`

## Pseudo-code:

```
function drake(x, c):
  for n=0 to N-1 do
    a[n] <- argmin_k d(x[n], c[k])                  # compute index of closest center
    u[n] <- min_k d(x[n], c[k])                     # compute exact distance to closest center
    for b=0 to B-1 do
      closestOtherCenters[i][j] = j + 1;


    l[n] <- argmin_(k!=a[n]) d(x[n], c[k])          # compute index of second closest center
    q[a[n]] <- q[a[n]] + 1                          # update cluster size
    for d=1 to D do                                 # update cluster sum for each dimension
      c'[a[n]][d] <- c'[a[n]][d] + x[n][d]          # update cluster sum for each dimension

  while not converged do
    for k=0 to K-1 do
      s[k] <- min_(k'!=k) d(c[k], c[k'])            # compute the nearest cluster center for each cluster center

    for n=0 to N-1 do                                 # compute the nearest cluster for each point
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
            for d=1 to D do                         # update cluster sum for each dimension
              c'[a'][d] <- c'[a'][d] - x[n][d]      # update cluster sum for each dimension
              c'[a[n]][d] <- c'[a[n]][d] + x[n][d]  # update cluster sum for each dimension

    for k=0 to K-1 do                                 # assign each cluster center to the average of its points
      c* <- c[k]                                    # store old center for later
      for d=1 to D do
        c[k][d] <- c'[k][d]/q[k]                    # cluster sum divided by cluster size
      p[k] <- d(c*,c[k])                            # store the distance that the center has moved

                                                    # update bounds
    r  <- argmax_k p                                # index of the center that has moved the most
    r' <- argmax_k!=r p                             # index of the center that has moved the second most
    for n=0 to N-1 do
      u[n] <- u[n] + p[a[n]]                        # increase upper bound by the distance that the assigned center has moved
      if r=a[i] then
        l[n] <- l[n] - p[r']                        # decrease by the second most distance moved
      else
        l[n] <- l[n] - p[r]                         # decrease by the most distance moved
```
