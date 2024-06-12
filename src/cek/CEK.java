package cek;

import continuations.*;
import exprs.*;
import values.*;

import java.math.BigInteger;
import java.util.List;

public class CEK {

  private IExpr control;
  private Environment env;
  private IContinuation kont;
  private Value<?> finalRes;
  private Value<?> nextK;

  public CEK(IExpr control, Environment env, IContinuation kont, Value<?> finalRes, Value<?> nextK) {
    this.control = control;
    this.env = env;
    this.kont = kont;
    this.finalRes = finalRes;
    this.nextK = nextK;
  }

  public CEK(IExpr control, Environment env, IContinuation kont) {
    this(control, env, kont, null, null);
  }

  public static CEK blankCEK(IExpr control) {
    return new CEK(control, new Environment(), new HaltKont());
  }

  public Value<?> run() {
    while (this.finalRes == null) {
      CEK next = this.step();
      this.control = next.control;
      this.env = next.env;
      this.kont = next.kont;
      this.finalRes = next.finalRes;
      this.nextK = next.nextK;
    }
    return this.finalRes;
  }

  public CEK step() {
    // If we have a value to invoke the continuation on, it is saved,
    // and we invoke it here. This avoids a stack overflow.
    if (this.nextK != null) {
      Value<?> arg = this.nextK;
      this.nextK = null;
      return new CEK(this.control, null, this.kont).invokeContinuation(arg);
    } else {
      return switch (this.control) {
        case IntConst ic -> this.invokeContinuation(new NumValue(ic.getVALUE()));
        case BoolConst bc -> this.invokeContinuation(new BoolValue(bc.getVALUE()));
        case EmptyConsConst ec -> this.invokeContinuation(new EmptyConsValue());
        case Var v -> this.invokeContinuation(this.env.lookup(v));
        case Lambda lam -> this.invokeContinuation(new Closure(lam, this.env));
        case Let(var id, var exp, var body) -> new CEK(new App(new Lambda(id, body), exp), this.env, this.kont);
        case If(var pred, var cons, var alt) -> new CEK(pred, this.env, new IfKont(cons, alt, this.env, this.kont));
        case UnaryOp(var op, var expr) -> new CEK(expr, this.env, new UnaryOpKont(op, null, this.env, this.kont));
        case BinOp(var op, var left, var right) -> new CEK(left, this.env, new BinOpKont(op, null, right, this.env, this.kont));
        case App(var rator, var rand) -> new CEK(rator, this.env, new ApplyKont(rand, this.env, this.kont));
        default -> throw new IllegalStateException("Unexpected value: " + this.control);
      };
    }
  }

  public CEK invokeContinuation(Value<?> value) {
    return switch (this.kont) {
      case HaltKont haltKont -> new CEK(null, null, null, value, null);
      case ApplyKont appKont -> new CEK(appKont.rand(), appKont.env(), new ArgKont((Closure) value, appKont.kont()));
      case IfKont ifKont -> new CEK(((BoolValue) value).getVALUE() ? ifKont.cons() : ifKont.alt(), ifKont.env(), ifKont.kont());
      case ArgKont argKont -> {
        // If it's an argument then its expr must be a closure.
        Closure clos = argKont.clos();
        Environment e1 = clos.getEnv().extend(List.of(clos.getVALUE().param()), List.of(value));
        yield new CEK(clos.getVALUE().body(), e1, argKont.kont());
      }
      case UnaryOpKont unaryOpKont -> {
        Value<?> result = switch (unaryOpKont.op()) {
          case "car" -> ((ConsValue) value).getCAR();
          case "cdr" -> ((ConsValue) value).getCDR();
          case "not" -> new BoolValue(!((BoolValue) value).getVALUE());
          case "null?" -> new BoolValue(value instanceof EmptyConsValue);
          default -> throw new IllegalStateException("ERR invalid op " + unaryOpKont.op());
        };
        yield new CEK(null, null, unaryOpKont.kont(), null, result);
      }
      case BinOpKont binOpCont -> {
        // First, evaluate the left child; the right is evaluated after.
        if (binOpCont.leftValue() == null) {
          yield new CEK(binOpCont.rightValue(), binOpCont.env(), new BinOpKont(binOpCont.op(), value, binOpCont.rightValue(), binOpCont.env(), binOpCont.kont()));
        } else {
          // If the operator is "numeric", then we can interpret the values as numbers.
          Value<?> result = null;
          if (binOpCont.isConsOp()) {
            result = switch (binOpCont.op()) {
              case "cons" -> new ConsValue(binOpCont.leftValue(), value);
              default -> throw new IllegalStateException("ERR invalid op " + binOpCont.op());
            };
          } else if (binOpCont.isBooleanOp()) {
            result = switch (binOpCont.op()) {
              case "eq?" -> new BoolValue(value.equals(binOpCont.leftValue()));
              case "and" -> new BoolValue(((BoolValue) binOpCont.leftValue()).getVALUE() && ((BoolValue) value).getVALUE());
              case "or" -> new BoolValue(((BoolValue) binOpCont.leftValue()).getVALUE() || ((BoolValue) value).getVALUE());
              default -> throw new IllegalStateException("err invalid op " + binOpCont.op());
            };
          } else if (binOpCont.isNumericOp()) {
            // Both values exist, so use both!
            BigInteger left = ((NumValue) binOpCont.leftValue()).getVALUE();
            BigInteger right = ((NumValue) value).getVALUE();
            result = switch (binOpCont.op()) {
              case "+" -> new NumValue(left.add(right));
              case "-" -> new NumValue(left.subtract(right));
              case "*" -> new NumValue(left.multiply(right));
              case "/" -> new NumValue(left.divide(right));
              case "<" -> new BoolValue(left.compareTo(right) < 0);
              case "<=" -> new BoolValue(left.compareTo(right) <= 0);
              case ">" -> new BoolValue(left.compareTo(right) > 0);
              case ">=" -> new BoolValue(left.compareTo(right) >= 0);
              default -> throw new IllegalStateException("ERR invalid op " + binOpCont.op());
            };
          } else {
            throw new IllegalStateException("ERR invalid op " + binOpCont.op());
          }
          yield new CEK(null, null, binOpCont.kont(), null, result);
        }
      }
      case null, default -> throw new IllegalStateException("Unknown continuation type " + this.kont + " for value + " + value);
    };
  }

  @Override
  public String toString() {
    return String.format("%s, %s, %s", this.control, this.env, this.kont);
  }
}
