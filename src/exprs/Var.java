package exprs;

public record Var(String id) implements IExpr {

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Var v) && this.id.equals(v.id);
  }

  @Override
  public String toString() {
    return this.id;
  }
}
