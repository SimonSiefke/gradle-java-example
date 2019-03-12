package kmeans.lloyd;

import kmeans.KMeansStrategyTestBase;

public class OriginalLloydKMeansStrategyTest extends KMeansStrategyTestBase<OriginalLloydKMeansStrategy> {
  @Override
  protected OriginalLloydKMeansStrategy createInstance() {
    return new OriginalLloydKMeansStrategy();
  }
}
