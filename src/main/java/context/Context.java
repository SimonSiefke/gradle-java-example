package context;

/**
 * Context for choosing different strategies.
 */
public class Context<T> {
  protected T strategy;

  protected Context() {
    // protected constructor to prevent creating objects of this class.
  }

  public void setStrategy(T strategy) {
    this.strategy = strategy;
  }
}
