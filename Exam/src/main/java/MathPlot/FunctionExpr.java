package MathPlot;

public class FunctionExpr implements Expr {

    public enum Type {
        SIN,
        COS
    }

    private final Type type;
    private final Expr arg;

    public FunctionExpr(Type type, Expr arg) {
        this.type = type;
        this.arg = arg;
    }

    @Override
    public double eval(double x) {
        double v = arg.eval(x);

        switch (type) {
            case SIN:
                return Math.sin(v);
            case COS:
                return Math.cos(v);
            default:
                throw new IllegalStateException("Unknown function type");
        }
    }

    @Override
    public Expr derive() {
        Expr innerDer = arg.derive();

        switch (type) {
            case SIN:
                // (sin f)' = cos(f) * f'
                return new BinaryOpExpr(
                        BinaryOpExpr.Op.MUL,
                        new FunctionExpr(Type.COS, arg),
                        innerDer
                );

            case COS:
                // (cos f)' = -sin(f) * f'
                Expr negativeSin = new BinaryOpExpr(
                        BinaryOpExpr.Op.MUL,
                        new ConstantExpr(-1.0),
                        new FunctionExpr(Type.SIN, arg)
                );
                return new BinaryOpExpr(
                        BinaryOpExpr.Op.MUL,
                        negativeSin,
                        innerDer
                );

            default:
                throw new IllegalStateException("Unknown function type");
        }
    }

    @Override
    public String toRpnString() {
        String name = type == Type.SIN ? "sin" : "cos";
        return arg.toRpnString() + " " + name;
    }

    @Override
    public String toAosString() {
        String name = type == Type.SIN ? "sin" : "cos";
        return name + "(" + arg.toAosString() + ")";
    }
}
