package cek;

import exprs.*;
import values.NumValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CEKTester {

  @org.junit.jupiter.api.Test
  void run() {
    // ((lambda (x) (+ x 2) (* 3 4))
    IExpr e1 = new App(
            new Lambda("x", new BinOp("+", new Var("x"), new IntConst(2))),
            new BinOp("*", new IntConst(3), new IntConst(4)));
    assertEquals(new NumValue(14), CEK.blankCEK(e1).run());

    // (+ 1 (* 2 3))
    IExpr e2 = new BinOp("+", new IntConst(1),
            new BinOp("*", new IntConst(2), new IntConst(3)));
    assertEquals(new NumValue(7), CEK.blankCEK(e2).run());

    // (- 2 1)
    IExpr e3 = new BinOp("-", new IntConst(2), new IntConst(1));
    assertEquals(new NumValue(1), CEK.blankCEK(e3).run());

    // ((lambda (x) (if #t (+ x 2) (* x 2))) (+ 3 4))
    IExpr e4 = new App(new Lambda("x", new If(new BoolConst(true),
            new BinOp("+", new Var("x"), new IntConst(2)),
            new BinOp("*", new Var("x"), new IntConst(2)))),
            new BinOp("+", new IntConst(3), new IntConst(4)));
    assertEquals(new NumValue(9), CEK.blankCEK(e4).run());

    // (let ([x (+ 2 3)]) x) => ((lambda (x) x) (+ 2 3))
    IExpr e5 = new Let("x", new BinOp("+", new IntConst(2), new IntConst(3)), new Var("x"));
    assertEquals(new NumValue(5), CEK.blankCEK(e5).run());

    /*
    (let ([f (λ (f)
           (λ (n)
             (if (= n 0)
                 1
                 (* n ((f f) (- n 1))))))])
      ((f f) 5))
     */
    IExpr e6 = new Let("f",
            new Lambda("f",
                    new Lambda("n",
                            new If(new BinOp("eq?", new Var("n"), new IntConst(0)),
                                    new IntConst(1),
                                    new BinOp("*", new Var("n"), new App(new App(new Var("f"), new Var("f")), new BinOp("-", new Var("n"), new IntConst(1))))))),
            new App(new App(new Var("f"), new Var("f")), new IntConst(5)));
    assertEquals(new NumValue(120), CEK.blankCEK(e6).run());

    IExpr e7 = new Let("f",
            new Lambda("f",
                    new Lambda("n",
                            new If(new BinOp("eq?", new Var("n"), new IntConst(0)),
                                    new IntConst(1),
                                    new BinOp("*", new Var("n"), new App(new App(new Var("f"), new Var("f")), new BinOp("-", new Var("n"), new IntConst(1))))))),
            new App(new App(new Var("f"), new Var("f")), new IntConst(500000)));
    CEK.blankCEK(e7).run();
//    assertEquals(new NumValue("120"), CEK.blankCEK(e7).run());
  }
}