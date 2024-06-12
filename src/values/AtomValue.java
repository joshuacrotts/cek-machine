package values;

public class AtomValue extends Value<String> {

  public AtomValue(String value) {
    super(value);
  }

  @Override
  public String toString() {
    return this.getVALUE();
  }
}
