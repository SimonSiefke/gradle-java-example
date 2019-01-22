# Drake's K-Means Algorithm

Drake's Algorithm (also known as Adaptive KMeans) tries to optimize Lloyd's Algorithm by using pruning techniques to skip distance calculations and increase performance, while yielding the same results as Lloyd's Algorithm in the end and also after each iteration. It also needs the same number of iterations to converge.

1. The pruning technique uses N upper bounds (1 for each data point) and B lower bounds (1<B<K, the value of B changes over time). For each point the upper bound is the maximal possible distance between data point and its currently assigned center. The first (B-1) lower bounds contain the minimal distance between the data point and its (B+2)-closest centers (e.g. l[0] is a bound on distance to the second closest center for a data point, l[1] on the distance third closest center). The last lower bound (l[B-1]) is how far every other center (that is not in the bounds l[0]...l[B-2]) minimally is, it is a lower bound for the rest. The lower bounds are always kept in that order, from smallest distance to largest distance (keeping the order is one of the more complex parts of this algorithm). Now we can use this to prune:
   If the second closest center for a point (l[0]) is minimally further away than the closest center is maximally away (upper bound) we know that the point keeps its assigned center and we don't need to compute the distance to any other center.

## Variables:

| Type                   | Memory | Name | Purpose                                                                                                                |
| ---------------------- | ------ | ---- | ---------------------------------------------------------------------------------------------------------------------- |
| `int`                  |        | `D`  | number of dimensions                                                                                                   |
| `int`                  |        | `K`  | number of clusters (and cluster centers)                                                                               |
| `int`                  |        | `N`  | number of data points                                                                                                  |
| `int[N]`               | `N`    | `a`  | for each data point the index of the cluster it is assigned to                                                         |
| `double[K][D]`         | `KD`   | `c`  | cluster centers                                                                                                        |
| `int[N][B]`            | `NB`   | `co` | for each data point the indices of its B closest other cluster centers                                                 |
| `double[K][D]`         | `KD`   | `c'` | for each cluster the vector sum of all its points                                                                      |
| `double[N]`            | `NB`   | `l`  | for each data point B lower bounds on the distance to its second closest center, third closest center ... and the rest |
| `int[K]`               | `K`    | `q`  | for each cluster the number of its points                                                                              |
| `double[K]`            | `K`    | `p`  | for each cluster center the distance that it last moved                                                                |
| `Tuple<double,int>[K]` | `2K`   | `o`  | the order of the cluster centers (used to sort centers for a point)                                                    |
| `double[K]`            | `K`    | `s`  | for each cluster center the distance to its closest other center                                                       |
| `double[N]`            | `N`    | `u`  | for each point an upper bound on the distance to its closest center                                                    |
| `double[N][D]`         | `ND`   | `x`  | data points                                                                                                            |

Total Additional Memory: `2NB + 2N + KD + 5K`

## Pseudo-code:

```
function drake(x, c):
  for n=0 to N-1 do
    a[n] <- argmin_k d(x[n], c[k])                  # compute index of closest center
    u[n] <- min_k d(x[n], c[k])                     # compute exact distance to closest center
    for b=0 to B-1 do
      l[n][b] = b + 1;                              # assign initial lower bound
    q[a[n]] <- q[a[n]] + 1                          # update cluster size
    for d=1 to D do                                 # update cluster sum for each dimension
      c'[a[n]][d] <- c'[a[n]][d] + x[n][d]          # update cluster sum for each dimension

  while not converged do
    for n=0 to N-1 do                               # compute the nearest cluster for each point
      for b=0 to B-1 do
        if u[n]<=l[n][b] then                       # bound test
          o[0].first <- d(x[n], a[n])
          o[0].second <- a[n]
          for b'=0 to b do                          # update the centers that got out of order
            o[b'+1].second <- co[n][b']
            o[b'+1].first <- d(x[n], o[b' + 1].second)
          sort(o)                                   # reorder the centers that got out of order

          if a[n] != o[0].second then               # when the closest cluster index hasn't changed
            q[a'] <- q[a'] - 1                      # update cluster size
            q[a[n]] <- q[a[n]] + 1                  # update cluster size
            for d=0 to D-1 do                       # update cluster sum for each dimension
              c'[a'][d] <- c'[a'][d] - x[n][d]      # update cluster sum for each dimension
              c'[a[n]][d] <- c'[a[n]][d] + x[n][d]  # update cluster sum for each dimension

          u[n] <- o[0].first
          for b'=0 to b do
            co[n][b'] <- o[b'+1].second             # update closest other centers
            l[n][b'] <- o[b'+1].first               # update lower bound
          skip to next iteration of for n loop
      for k=0 to K do
        o[k].first <- d(x[n], c[k])                 # compute distance to every center for this point
        o[k].second <- k                            # also store index so that we can sort
      sort(o)                                       # sort order by increasing distance from x
      if a[n] != o[0].second then                   # when the closest cluster index hasn't changed
            q[a'] <- q[a'] - 1                      # update cluster size
            q[a[n]] <- q[a[n]] + 1                  # update cluster size
            for d=0 to D-1 do                       # update cluster sum for each dimension
              c'[a'][d] <- c'[a'][d] - x[n][d]      # update cluster sum for each dimension
              c'[a[n]][d] <- c'[a[n]][d] + x[n][d]  # update cluster sum for each dimension
      u[n]<- o[0]                                   # exact distance to assigned center
      for b=0 to B do
        co[n][b] <- o[b + 1].second                 # update closest other centers
        l[n][b] <- o[b + 1].first                   # update lower bounds (they start with second closest center, therefore b+1)

    for k=0 to K-1 do                               # assign each cluster center to the average of its points
      c* <- c[k]                                    # store old center for later
      for d=1 to D do
        c[k][d] <- c'[k][d]/q[k]                    # cluster sum divided by cluster size
      p[k] <- d(c*,c[k])                            # store the distance that the center has moved

                                                    # update bounds
    r  <- argmax_k p                                # index of the center that has moved the most
    for n=0 to N-1 do
      u[n] <- u[n] + p[a[n]]                        # increase upper bound by the distance that the assigned center has moved
      for b=0 to B-2 do                             # Update all lower bounds but the lower bound for the rest
        l[n][b] <- l[n][b] - co[n][b]               # Shrink the lower bound by the distance its center has moved
      l[n][B-1] <- l[n][B-1] - p[r]                 # decrease lower bound for the rest by the maximum distance moved
      for b=B-2 to 0 do                             # reorder lower bounds
        if l[n][b+1] < l[n][b] then
          l[n][b] <- l[n][b+1]
```
