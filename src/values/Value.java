package values;

import java.util.Objects;

public abstract class Value<T> {

  private final T VALUE;

  public Value(T v) {
    this.VALUE = v;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Value<?> value1)) return false;
    return Objects.equals(this.VALUE, value1.VALUE);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.VALUE);
  }

  @Override
  public String toString() {
    return "Value{" +
            "value=" + VALUE +
            '}';
  }

  public T getVALUE() {
    return this.VALUE;
  }
}
