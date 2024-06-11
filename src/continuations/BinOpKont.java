package continuations;

import cek.Environment;
import exprs.IExpr;
import values.Value;

public class BinOpKont implements IContinuation {

  private final String OP;
  private final Environment ENV;
  private final IExpr RIGHT_VALUE;
  private final IContinuation KONT;

  private Value<?> leftValue;

  public BinOpKont(String op, Value<?> leftValue, IExpr rightValue, Environment env, IContinuation kont) {
    this.OP = op;
    this.ENV = env;
    this.RIGHT_VALUE = rightValue;
    this.KONT = kont;
    this.leftValue = leftValue;
  }

  @Override
  public String toString() {
    return "BinOpCont{" +
            "OP='" + this.OP + '\'' +
            ", ENV=" + this.ENV +
            ", RIGHT_VALUE=" + this.RIGHT_VALUE +
            ", KONT=" + this.KONT +
            ", leftValue=" + this.leftValue +
            '}';
  }

  public String getOp() {
    return this.OP;
  }

  public Value<?> leftValue() {
    return this.leftValue;
  }

  public void setLeftValue(Value<?> v) {
    this.leftValue = v;
  }

  public Environment env() {
    return this.ENV;
  }

  public IExpr rightValue() {
    return this.RIGHT_VALUE;
  }

  public IContinuation kont() {
    return this.KONT;
  }
}
