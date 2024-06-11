package values;

import java.util.Objects;

public abstract class Value<T> {

  private final T value;

  public Value(T v) {
    this.value = v;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Value<?> value1)) return false;
    return Objects.equals(this.value, value1.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.value);
  }

  @Override
  public String toString() {
    return "Value{" +
            "value=" + value +
            '}';
  }

  public T getValue() {
    return this.value;
  }
}
