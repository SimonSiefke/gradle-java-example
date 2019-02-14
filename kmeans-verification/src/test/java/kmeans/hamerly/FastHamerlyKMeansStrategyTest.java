package kmeans.hamerly;

import kmeans.KMeansStrategyTestBase;

public class FastHamerlyKMeansStrategyTest extends KMeansStrategyTestBase<FastHamerlyKMeansStrategy> {
  @Override
  protected FastHamerlyKMeansStrategy createInstance() {
    return new FastHamerlyKMeansStrategy();
  }
}
