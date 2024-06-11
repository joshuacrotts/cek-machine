package exprs;

public abstract class Const<T> implements IExpr {

  private final T value;

  public Const(T v) {
    this.value = v;
  }

  public T getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
