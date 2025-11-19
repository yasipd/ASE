package MathPlot;

public class VariableExpr implements Expr {
    @Override
    public double eval(double x) {
        return x;
    }

    @Override
    public Expr derive() {
        return new ConstantExpr(1.0);
    }

    @Override
    public String toRpnString() {
        return "x";
    }

    @Override
    public String toAosString() {
        return "x";
    }
}
