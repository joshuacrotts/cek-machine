package continuations;

import cek.Environment;
import exprs.IExpr;

public record IfKont(IExpr cons, IExpr alt, Environment env, IContinuation kont) implements IContinuation {
}
