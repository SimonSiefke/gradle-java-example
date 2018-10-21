package kmeans.lloyd;

import kmeans.KMeansTestBase;

public class LloydKMeansStrategyTest extends KMeansTestBase<LloydKMeansStrategy> {

  @Override
  protected LloydKMeansStrategy createInstance() {
    return new LloydKMeansStrategy();
  }
}
