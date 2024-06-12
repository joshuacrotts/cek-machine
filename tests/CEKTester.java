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

    IExpr e16 = new BinOp("eq?",
            new UnaryOp("car", new BinOp("cons", new AtomConst("+"), new EmptyConsConst())),
            new AtomConst("+"));
    assertEquals(new BoolValue(true), CEK.blankCEK(e16).run());

    /*
    (let ([env '()])
  (let ([cadr (λ (p) (car (cdr p)))])
    (let ([caddr (λ (p) (car (cdr (cdr p))))])
      (let ([lookup (λ (f)
                      (λ (v)
                        (λ (env)
                          (if (null? env)
                              (error "bad var ~a\n" v)
                              (let ([pair (car env)])
                                (if (eq? (car pair) v)
                                    (cdr pair)
                                    (((f f) v) env)))))))])
        (let ([plus? (λ (exp)
                       (eq? (car exp) '+))])
          (let ([eval (λ (f)
                        (λ (exp)
                          (λ (env)
                            (if (number? exp)
                                exp
                                (if (plus? exp)
                                    (let ([fst (cadr exp)])
                                      (let ([snd (caddr exp)])
                                        (+ (((f f) fst) env)
                                           (((f f) snd) env))))
                                    (error "bad exp"))))))])
            (((eval eval) (cons '+ (cons 1 (cons 2 empty)))) env)))))))
     */
    IExpr e15 = new Let("env", new EmptyConsConst(),
            new Let("cadr", new Lambda("p", new UnaryOp("car", new UnaryOp("cdr", new Var("p")))),
                    new Let("caddr", new Lambda("p", new UnaryOp("car", new UnaryOp("cdr", new UnaryOp("cdr", new Var("p"))))),
                            new Let("lookup", new Lambda("f",
                                    new Lambda("v",
                                            new Lambda("env",
                                                    new If(new UnaryOp("null?", new Var("env")),
                                                            new IntConst(0),
                                                            new Let("pair", new UnaryOp("car", new Var("env")),
                                                                    new If(new BinOp("eq?", new UnaryOp("car", new Var("pair")), new Var("v")),
                                                                            new UnaryOp("cdr", new Var("pair")),
                                                                            new App(new App(new App(new Var("f"), new Var("f")), new Var("v")), new UnaryOp("cdr", new Var("env"))))))))),
                                    new Let("plus?", new Lambda("exp", new BinOp("eq?", new UnaryOp("car", new Var("exp")), new AtomConst("+"))),
                                            new Let("eval", new Lambda("f",
                                                    new Lambda("exp",
                                                            new Lambda("env",
                                                                    new If(new UnaryOp("number?", new Var("exp")),
                                                                            new Var("exp"),
                                                                            new If(new App(new Var("plus?"), new Var("exp")),
                                                                                   new Let("fst", new App(new Var("cadr"), new Var("exp")),
                                                                                            new Let("snd", new App(new Var("caddr"), new Var("exp")),
                                                                                                    new BinOp("+",
                                                                                                            new App(new App(new App(new Var("f"), new Var("f")), new Var("fst")), new Var("env")),
                                                                                                            new App(new App(new App(new Var("f"), new Var("f")), new Var("snd")), new Var("env"))))),
                                                                                    new IntConst(0)))))),
                                                    new App(new App(new App(new Var("eval"), new Var("eval")),
                                                                    new BinOp("cons", new AtomConst("+"), new BinOp("cons", new IntConst(1), new BinOp("cons", new IntConst(2), new EmptyConsConst())))), new Var("env"))))))));
    assertEquals(new NumValue(3), CEK.blankCEK(e15).run());
  }
}