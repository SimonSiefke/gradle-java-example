# Elkan's KMeans Algorithm

Elkan's algorithm is a variation of Lloyd's algorithm that uses triangle inequality to avoid some distance computations when assigning points to clusters.

Pseudo-code:

```
function elkan(inputData, clusters):
  while the clusters change:
    for all points:
      compute the closest cluster center and store the point in this cluster

    for all clusters:
      set the cluster center to the average of the points in the cluster
```
