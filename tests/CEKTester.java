import cek.CEK;
import exprs.*;
import values.BoolValue;
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

    /*
    (let ([f (λ (f)
           (λ (n)
             (if (= n 0)
                 1
                 (* n ((f f) (- n 1))))))])
      ((f f) 500000))
     */
    IExpr e7 = new Let("f",
            new Lambda("f",
                    new Lambda("n",
                            new If(new BinOp("eq?", new Var("n"), new IntConst(0)),
                                    new IntConst(1),
                                    new BinOp("*", new Var("n"), new App(new App(new Var("f"), new Var("f")), new BinOp("-", new Var("n"), new IntConst(1))))))),
            new App(new App(new Var("f"), new Var("f")), new IntConst(500000)));

    /*
    (car (cons 1 2))
     */
    IExpr e8 = new UnaryOp("car", new BinOp("cons", new IntConst(1), new IntConst(2)));
    assertEquals(new NumValue(1), CEK.blankCEK(e8).run());

    /*
    (null? (cons 1 ()))
     */
    IExpr e9 = new UnaryOp("null?", new BinOp("cons", new IntConst(1), new EmptyConsConst()));
    assertEquals(new BoolValue(false), CEK.blankCEK(e9).run());

    /*
    (null? ())
     */
    IExpr e10 = new UnaryOp("null?", new EmptyConsConst());
    assertEquals(new BoolValue(true), CEK.blankCEK(e10).run());

    /*
    (let ([f (λ (f)
           (λ (l)
             (if (null? l)
                 0
                 (+ 1 ((f f) (cdr l))))))])
        ((f f) (cons 100 (cons 200 (cons 230 (cons 400 (cons 2000 empty)))))))
     */
    IExpr e11 = new Let("f",
            new Lambda("f",
                    new Lambda("l",
                            new If(new UnaryOp("null?", new Var("l")),
                                    new IntConst(0),
                                    new BinOp("+", new IntConst(1), new App(new App(new Var("f"), new Var("f")), new UnaryOp("cdr", new Var("l"))))))),
            new App(new App(new Var("f"), new Var("f")),
                    new BinOp("cons", new IntConst(100),
                            new BinOp("cons", new IntConst(200),
                                    new BinOp("cons", new IntConst(230),
                                            new BinOp("cons", new IntConst(400),
                                                    new BinOp("cons", new IntConst(2000), new EmptyConsConst())))))));
    assertEquals(new NumValue(5), CEK.blankCEK(e11).run());

    /*
    (let ([f (λ (f)
           (λ (n)
             (if (<= n 1)
                 n
                 (+ ((f f) (- n 1))
                    ((f f) (- n 2))))))])
      ((f f) 10))
     */
    IExpr e12 = new Let("f",
            new Lambda("f",
                    new Lambda("n",
                            new If(new BinOp("<=", new Var("n"), new IntConst(1)),
                                    new Var("n"),
                                    new BinOp("+",
                                            new App(new App(new Var("f"), new Var("f")), new BinOp("-", new Var("n"), new IntConst(1))),
                                            new App(new App(new Var("f"), new Var("f")), new BinOp("-", new Var("n"), new IntConst(2))))))),
            new App(new App(new Var("f"), new Var("f")), new IntConst(10)));
    assertEquals(new NumValue(55), CEK.blankCEK(e12).run());

    /*
    (car (cons (+ 1 2) 3))
     */
    IExpr e13 = new UnaryOp("car", new BinOp("cons", new BinOp("+", new IntConst(1), new IntConst(2)), new IntConst(3)));
    assertEquals(new NumValue(3), CEK.blankCEK(e13).run());

    /*
    (cdr (cons 100 (* 2 3)))
     */
    IExpr e14 = new UnaryOp("cdr", new BinOp("cons", new IntConst(100), new BinOp("*", new IntConst(2), new IntConst(3))));
    assertEquals(new NumValue(6), CEK.blankCEK(e14).run());
  }
}