package util.dataloader;

/**
 * Facade for all the Data Loader Strategies that are available.
 */
public final class DataLoader {
  public static final DataLoaderStrategy TEXT = new TextDataLoaderStrategy();

  private DataLoader() {
    // private constructor to prevent creating objects of this class.
  }
}
