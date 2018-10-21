package dataloader;

/**
 * Facade for all the Data Loader Strategies that are available.
 */
public final class DataLoader {
  private static final DataLoaderStrategy TEXT = new TextDataLoaderStrategy();

  public static double[][] TEXT(String path) {
    return TEXT.load(path);
  }

  private DataLoader() {
    // private constructor to prevent creating objects of this class.
  }
}
