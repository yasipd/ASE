package MathPlot;

public interface Expr {
    double eval(double x);      // evaluate at x
    Expr derive();              // first derivative
    String toRpnString();       // RPN print
    String toAosString();       // “normal” print
}
