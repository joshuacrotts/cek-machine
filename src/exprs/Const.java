package exprs;

public abstract class Const<T> implements IExpr {

  private final T VALUE;

  public Const(T v) {
    this.VALUE = v;
  }

  public T getVALUE() {
    return this.VALUE;
  }

  @Override
  public String toString() {
    return VALUE.toString();
  }
}
