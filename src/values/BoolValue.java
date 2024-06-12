package values;

public class BoolValue extends Value<Boolean> {

  public BoolValue(boolean value) {
    super(value);
  }

  @Override
  public String toString() {
    return this.getVALUE().toString();
  }
}
