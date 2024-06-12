package continuations;

import cek.Environment;
import exprs.IExpr;

public record ApplyKont(IExpr rand, Environment env, IContinuation kont) implements IContinuation { }
