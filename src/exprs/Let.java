package exprs;

public record Let(String id, IExpr exp, IExpr body) implements IExpr {

  @Override
  public String toString() {
    return String.format("(let ([%s %s]) %s)", this.id, this.exp, this.body);
  }
}
