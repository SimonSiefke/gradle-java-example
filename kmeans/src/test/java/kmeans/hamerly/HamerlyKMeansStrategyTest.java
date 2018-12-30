package kmeans.hamerly;

import kmeans.KMeansStrategyTestBase;

public class HamerlyKMeansStrategyTest extends KMeansStrategyTestBase<HamerlyKMeansStrategy> {
  @Override
  protected HamerlyKMeansStrategy createInstance() {
    return new HamerlyKMeansStrategy();
  }
}
