package exprs;

public record App(IExpr rator, IExpr rand) implements IExpr {

  @Override
  public String toString() {
    return String.format("(%s %s)", this.rator, this.rand);
  }
}
