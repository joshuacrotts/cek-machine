package values;

public class ConsValue extends Value<Object> {

  private final Value<?> CAR;
  private final Value<?> CDR;

  public ConsValue(Value<?> car, Value<?> cdr) {
    super(null);
    this.CAR = car;
    this.CDR = cdr;
  }

  public Value<?> getCAR() {
    return this.CAR;
  }

  public Value<?> getCDR() {
    return this.CDR;
  }
}
