package exprs;

public record EmptyConsConst() implements IExpr {

  @Override
  public String toString() {
    return "()";
  }
}
