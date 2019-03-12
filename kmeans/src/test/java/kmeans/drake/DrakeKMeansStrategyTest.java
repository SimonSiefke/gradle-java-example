package kmeans.drake;

import kmeans.janis.JanisDrakeKMeansStrategyTestBase;

public class DrakeKMeansStrategyTest extends JanisDrakeKMeansStrategyTestBase<DrakeKMeansStrategy> {
  @Override
  protected DrakeKMeansStrategy createInstance() {
    return new DrakeKMeansStrategy();
  }
}
