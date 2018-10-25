package dataloader;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TextLoaderStrategyTest {

  @Test
  public void testFile() {
    DataLoader.TEXT("../kmeans/src/test/data/test_data_loader.txt");
  }

  @Test
  public void testFileWithOtherFormat() {
    DataLoader.TEXT("../kmeans/src/test/data/test_data_loader_with_other_format.txt");
  }

  @Test
  public void testNonExistingFile() {
    assertThrows(Exception.class, () -> DataLoader.TEXT("this path does not exist"));
  }
}
