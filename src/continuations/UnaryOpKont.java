package continuations;

import cek.Environment;
import exprs.IExpr;

public class UnaryOpKont implements IContinuation {

  private final String op;
  private final Environment env;
  private final IExpr rest;
  private final IContinuation kont;

  public UnaryOpKont(String op, IExpr rest, Environment env, IContinuation kont) {
    this.op = op;
    this.rest = rest;
    this.env = env;
    this.kont = kont;
  }

  public String op() {
    return this.op;
  }

  public IExpr rest() {
    return this.rest;
  }

  public Environment env() {
    return this.env;
  }

  public IContinuation kont() {
    return this.kont;
  }
}
