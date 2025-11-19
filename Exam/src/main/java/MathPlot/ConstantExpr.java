package MathPlot;

public class ConstantExpr implements Expr {
    private final double value;

    public ConstantExpr(double value) {
        this.value = value;
    }
// later for derivation of the power 
    public double getValue() {
        return value;
    }

    @Override
    public double eval(double x) {
        return value;
    }

    @Override
    public Expr derive() {
        return new ConstantExpr(0.0);
    }

    @Override
    public String toRpnString() {
        return Double.toString(value);
    }

    @Override
    public String toAosString() {
        return Double.toString(value);
    }
}