package exprs;

import java.math.BigInteger;

public class IntConst extends Const<BigInteger> {

  public IntConst(BigInteger v) {
    super(v);
  }

  public IntConst(int v) {
    this(BigInteger.valueOf(v));
  }
}
