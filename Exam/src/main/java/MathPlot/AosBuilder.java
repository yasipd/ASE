package MathPlot;

import MathPlot.Parsers.AOS;

public class AosBuilder {

    private final AOS parser = new AOS();

    public Expr build(String input) throws Exception {
        AOS.Parts p = parser.parse(input.trim());

        String main = p.main;

        // 1. Binary operators
        if (main.equals("+") || main.equals("-") ||
            main.equals("*") || main.equals("/") ||
            main.equals("^")) {

            Expr leftExpr = build(p.left);
            Expr rightExpr = build(p.right);

            return new BinaryOpExpr(toOp(main), leftExpr, rightExpr);
        }

        // 2. Variable x
        if (main.equals("x")) {
            return new VariableExpr();
        }

        // 3. Function sin/cos
        if (main.equals("sin")) {
            Expr arg = build(p.left);
            return new FunctionExpr(FunctionExpr.Type.SIN, arg);
        }

        if (main.equals("cos")) {
            Expr arg = build(p.left);
            return new FunctionExpr(FunctionExpr.Type.COS, arg);
        }

        // 4. Number literal
        try {
            double value = Double.parseDouble(main);
            return new ConstantExpr(value);
        } catch (NumberFormatException e) {
            throw new Exception("Unknown token: " + main);
        }
    }

    private BinaryOpExpr.Op toOp(String op) {
        switch (op) {
            case "+": return BinaryOpExpr.Op.ADD;
            case "-": return BinaryOpExpr.Op.SUB;
            case "*": return BinaryOpExpr.Op.MUL;
            case "/": return BinaryOpExpr.Op.DIV;
            case "^": return BinaryOpExpr.Op.POW;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }
}
