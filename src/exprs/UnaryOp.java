package exprs;

public record UnaryOp(String op, IExpr arg) implements IExpr {

  @Override
  public String toString() {
    return String.format("(%s %s)", this.op, this.arg);
  }
}
