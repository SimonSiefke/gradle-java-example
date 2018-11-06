# Possible improvements

## General

1. use partial distance instead of computing the full distance

## Elkan

1. only store and calculate half of the inter-cluster-distances because they are symmetric

1. optimize step 4 by calculating average inline (instead of first creating a list with all the points and then average)
