package dataloader;

/**
 * Interface for Distance Strategies.
 */
interface DataLoaderStrategy {
  /**
   * loads data from a file.
   *
   * @param path path of the file
   * @return the data points array
   */
  double[][] load(String path);
}
