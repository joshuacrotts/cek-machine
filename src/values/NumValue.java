package values;

import java.math.BigInteger;

public class NumValue extends Value<BigInteger> {

  public NumValue(int value) {
    super(BigInteger.valueOf(value));
  }

  public NumValue(BigInteger bi) {
    super(bi);
  }

  @Override
  public String toString() {
    return this.getVALUE().toString();
  }
}
