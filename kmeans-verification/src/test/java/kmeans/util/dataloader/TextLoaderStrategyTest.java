package src.test.java.kmeans.util.dataloader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import util.dataloader.DataLoader;

public class TextLoaderStrategyTest {

  @Test
  public void testFile() {
    DataLoader.TEXT("test_data/test_data_loader.txt");
  }

  @Test
  public void testWithoutExtension() {
    var exception = assertThrows(IllegalArgumentException.class, () -> DataLoader.TEXT("test_data/test_data_loader"));
    assertEquals("The file must end with \".txt\"", exception.getMessage());
  }

  @Test
  public void testFileWithOtherFormat() {
    DataLoader.TEXT("test_data/test_data_loader_with_other_format.txt");
  }

  @Test
  public void testNonExistingFile() {
    assertThrows(Exception.class, () -> DataLoader.TEXT("this path does not exist"));
  }
}
