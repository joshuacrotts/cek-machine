package continuations;

import cek.Environment;
import exprs.IExpr;
import values.Value;

public class BinOpKont implements IContinuation {

  private final String OP;
  private final Environment ENV;
  private final Value<?> LEFT_VALUE;
  private final IExpr RIGHT_VALUE;
  private final IContinuation KONT;

  public BinOpKont(String op, Value<?> leftValue, IExpr rightValue, Environment env, IContinuation kont) {
    this.OP = op;
    this.ENV = env;
    this.RIGHT_VALUE = rightValue;
    this.KONT = kont;
    this.LEFT_VALUE = leftValue;
  }

  @Override
  public String toString() {
    return "BinOpCont{" +
            "OP='" + this.OP + '\'' +
            ", ENV=" + this.ENV +
            ", RIGHT_VALUE=" + this.RIGHT_VALUE +
            ", KONT=" + this.KONT +
            ", leftValue=" + this.LEFT_VALUE +
            '}';
  }

  public boolean isConsOp() {
    return this.OP.equals("cons");
  }

  public boolean isNumericOp() {
    return this.OP.equals("+") || this.OP.equals("-") || this.OP.equals("*") || this.OP.equals("/") || this.OP.equals("<") || this.OP.equals("<=") || this.OP.equals(">") || this.OP.equals(">=");
  }

  public boolean isBooleanOp() {
    return this.OP.equals("&&") || this.OP.equals("||") || this.OP.equals("eq?") || this.OP.equals("not");
  }

  public String op() {
    return this.OP;
  }

  public Value<?> leftValue() {
    return this.LEFT_VALUE;
  }

  public IExpr rightValue() {
    return this.RIGHT_VALUE;
  }

  public Environment env() {
    return this.ENV;
  }

  public IContinuation kont() {
    return this.KONT;
  }
}
