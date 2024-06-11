package exprs;

public record If(IExpr pred, IExpr cons, IExpr alt) implements IExpr {

  @Override
  public String toString() {
    return String.format("(if %s %s %s)", this.pred, this.cons, this.alt);
  }
}
