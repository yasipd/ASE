package MathPlot;

public class BinaryOpExpr implements Expr {

    public enum Op {
        ADD, SUB, MUL, DIV, POW
    }

    private final Op op;
    private final Expr left;
    private final Expr right;

    public BinaryOpExpr(Op op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public double eval(double x) {
        double a = left.eval(x);
        double b = right.eval(x);

        switch (op) {
            case ADD:
                return a + b;
            case SUB:
                return a - b;
            case MUL:
                return a * b;
            case DIV:
                return a / b;
            case POW:
                return Math.pow(a, b);
            default:
                throw new IllegalStateException("Unknown op");
        }
    }

    @Override
    public Expr derive() {
        switch (op) {
            case ADD:
                // (f + g)' = f' + g'
                return new BinaryOpExpr(Op.ADD, left.derive(), right.derive());

            case SUB:
                // (f - g)' = f' - g'
                return new BinaryOpExpr(Op.SUB, left.derive(), right.derive());

            case MUL:
                // (f * g)' = f' * g + f * g'
                Expr part1 = new BinaryOpExpr(Op.MUL, left.derive(), right);
                Expr part2 = new BinaryOpExpr(Op.MUL, left, right.derive());
                return new BinaryOpExpr(Op.ADD, part1, part2);

            case DIV:
                // (f / g)' = (f' * g - f * g') / g^2
                Expr num1 = new BinaryOpExpr(Op.MUL, left.derive(), right);
                Expr num2 = new BinaryOpExpr(Op.MUL, left, right.derive());
                Expr numerator = new BinaryOpExpr(Op.SUB, num1, num2);
                Expr denominator = new BinaryOpExpr(Op.POW, right, new ConstantExpr(2.0));
                return new BinaryOpExpr(Op.DIV, numerator, denominator);

            case POW:
                // bare-minimum version: assume exponent is a constant n
                // (f^n)' = n * f^(n-1) * f'
                if (right instanceof ConstantExpr) {
                    double n = ((ConstantExpr) right).getValue();
                    Expr coef = new ConstantExpr(n);
                    Expr fPow = new BinaryOpExpr(Op.POW, left, new ConstantExpr(n - 1));
                    Expr inner = new BinaryOpExpr(Op.MUL, coef, fPow);
                    return new BinaryOpExpr(Op.MUL, inner, left.derive());
                }
                // general f(x)^g(x) would be more complex; for bare minimum we skip it
                return new ConstantExpr(0.0);

            default:
                throw new IllegalStateException("Unknown op");
        }
    }

    @Override
    public String toAosString() {
        String a = left.toAosString();
        String b = right.toAosString();

        switch (op) {
            case ADD:
                return "(" + a + " + " + b + ")";
            case SUB:
                return "(" + a + " - " + b + ")";
            case MUL:
                return "(" + a + " * " + b + ")";
            case DIV:
                return "(" + a + " / " + b + ")";
            case POW:
                return "(" + a + " ^ " + b + ")";
            default:
                throw new IllegalStateException("Unknown op");
        }
    }

    @Override
    public String toRpnString() {
        String a = left.toRpnString();
        String b = right.toRpnString();
        String opStr;

        switch (op) {
            case ADD:
                opStr = "+";
                break;
            case SUB:
                opStr = "-";
                break;
            case MUL:
                opStr = "*";
                break;
            case DIV:
                opStr = "/";
                break;
            case POW:
                opStr = "^";
                break;
            default:
                opStr = "?";
        }

        return a + " " + b + " " + opStr;
    }
}
