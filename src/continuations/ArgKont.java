package continuations;

import values.Closure;

public record ArgKont(Closure clos, IContinuation kont) implements IContinuation {
}
