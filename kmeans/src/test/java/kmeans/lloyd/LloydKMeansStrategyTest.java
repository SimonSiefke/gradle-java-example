package kmeans.lloyd;

import kmeans.KMeansStrategyTestBase;
import kmeans.lloyd.LloydKMeansStrategy;

public class LloydKMeansStrategyTest extends KMeansStrategyTestBase<LloydKMeansStrategy> {
  @Override
  protected LloydKMeansStrategy createInstance() {
    return new LloydKMeansStrategy();
  }
}
