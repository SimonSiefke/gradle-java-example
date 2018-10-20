package util.dataloader;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TextLoaderStrategyTest {

  @Test
  public void testFile() {
    DataLoader.TEXT.load("src/test/java/util/dataloader/data.txt");
  }

  @Test
  public void testFileWithOtherFormat() {
    DataLoader.TEXT.load("src/test/java/util/dataloader/data_with_other_format.txt");
  }

  @Test
  public void testNonExistingFile() {
    assertThrows(Exception.class, () -> DataLoader.TEXT.load("this path does not exist"));
  }
}
