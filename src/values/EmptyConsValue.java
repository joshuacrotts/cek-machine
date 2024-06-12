package values;

public class EmptyConsValue extends Value<Object> {

  public EmptyConsValue() {
    super(null);
  }

  @Override
  public String toString() {
    return "()";
  }
}
