package cek;

import exprs.Var;
import values.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {

  private final Environment PARENT;
  private final Map<String, Value> ADDRESSES;

  public Environment() {
    this(null);
  }

  public Environment(Environment parent) {
    this.PARENT = parent;
    this.ADDRESSES = new HashMap<>();
  }

  public Environment extend(List<String> formals, List<Value> addresses) {
    Environment e = new Environment(this);
    if (formals.size() != addresses.size()) {
      throw new IllegalArgumentException();
    } else {
      for (int i = 0; i < formals.size(); i++) {
        e.ADDRESSES.put(formals.get(i), addresses.get(i));
      }
      return e;
    }
  }

  public Value lookup(Var v) {
    for (String w : this.ADDRESSES.keySet()) {
      if (v.id().equals(w)) {
        return this.ADDRESSES.get(v.id());
      }
    }

    return this.PARENT != null ? this.PARENT.lookup(v) : null;
  }

  @Override
  public String toString() {
    return "cesk.Environment{" +
            "ADDRESSES=" + ADDRESSES +
            '}';
  }
}
