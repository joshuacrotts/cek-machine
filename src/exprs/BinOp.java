package exprs;

public record BinOp(String op, IExpr left, IExpr right) implements IExpr {

  @Override
  public String toString() {
    return String.format("(%s %s %s)", this.op, this.left, this.right);
  }
}
