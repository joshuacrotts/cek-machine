package values;

import cek.Environment;
import exprs.Lambda;

public class Closure extends Value<Lambda> {

  private final Environment ENV;

  public Closure(Lambda v, Environment env) {
    super(v);
    this.ENV = env;
  }

  public Environment getEnv() {
    return this.ENV;
  }
}
