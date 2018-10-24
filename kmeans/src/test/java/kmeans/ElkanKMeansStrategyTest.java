package kmeans;

import kmeans.elkan.ElkanKMeansStrategy;

public class ElkanKMeansStrategyTest extends KMeansStrategyTestBase<ElkanKMeansStrategy> {

  @Override
  protected ElkanKMeansStrategy createInstance() {
    return new ElkanKMeansStrategy();
  }

}
