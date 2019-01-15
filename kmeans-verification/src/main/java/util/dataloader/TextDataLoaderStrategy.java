package util.dataloader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Text Data Loader Strategy.
 */
class TextDataLoaderStrategy implements DataLoaderStrategy {
  @Override
  public double[][] load(String path) {
    if (!path.endsWith(".txt")) {
      throw new IllegalArgumentException("The file must end with \".txt\"");
    }
    try {

      InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
      Scanner scanner = new Scanner(is, "UTF-8");
      List<double[]> doubles = new ArrayList<>();
      while (scanner.hasNextLine()) {
        var line = scanner.nextLine().trim().replaceAll("\\s+", " ");
        var rawNumbers = line.split(" ");

        double[] numbersInLine = new double[rawNumbers.length];
        for (var i = 0; i < rawNumbers.length; i++) {
          numbersInLine[i] = Double.parseDouble(rawNumbers[i]);
        }
        doubles.add(numbersInLine);
      }
      scanner.close();
      return doubles.toArray(new double[][] {});
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
