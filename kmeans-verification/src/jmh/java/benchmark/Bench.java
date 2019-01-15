package benchmark;

import org.openjdk.jmh.annotations.Benchmark;

public class Bench {

  @Benchmark
  public void foo() {
    // DataLoader.TEXT("")
    // InputStream is = getClass().getClassLoader().getResourceAsStream("file.txt");
    // char[] buf = new char[100];
    // try {
    // InputStreamReader r = new InputStreamReader(is);
    // r.read(buf, 0, 100);
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // System.out.println(buf);
  }

}
