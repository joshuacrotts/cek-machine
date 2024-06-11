package exprs;

public record Lambda(String param, IExpr body) implements IExpr {

  @Override
  public String toString() {
    return String.format("(lambda (%s) %s)", this.param, this.body);
  }
}
