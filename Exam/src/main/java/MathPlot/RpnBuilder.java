package MathPlot;

import java.util.Stack;

import MathPlot.Parsers.RPN;

public class RpnBuilder {

    public Expr build(String input) throws Exception {
        //using RPN parser
        RPN parser = new RPN(input);
        Stack<String> tokens = parser.parse();

        //this stack will hold Expr nodes while building the tree
        Stack<Expr> exprStack = new Stack<>();

        //process tokens from left to right
        for (String token : tokens) {
            token = token.trim().toLowerCase();
            if (token.isEmpty()) {
                continue;
            }

            //number literal
            if (isNumber(token)) {
                double value = Double.parseDouble(token);
                exprStack.push(new ConstantExpr(value));
                continue;
            }

            //variable x
            if (token.equals("x")) {
                exprStack.push(new VariableExpr());
                continue;
            }

            //operator: + - * / ^
            if (isOperator(token)) {
                if (exprStack.size() < 2) {
                    throw new Exception("Not enough operands for operator: " + token);
                }
                Expr right = exprStack.pop();
                Expr left = exprStack.pop();
                BinaryOpExpr.Op op = toOp(token);
                exprStack.push(new BinaryOpExpr(op, left, right));
                continue;
            }

            // funnctions sin, cos 
            if (token.equals("sin") || token.equals("cos")) {
                if (exprStack.isEmpty()) {
                    throw new Exception("Not enough arguments for function: " + token);
                }
                Expr arg = exprStack.pop();
                FunctionExpr.Type type =
                        token.equals("sin") ? FunctionExpr.Type.SIN : FunctionExpr.Type.COS;
                exprStack.push(new FunctionExpr(type, arg));
                continue;
            }

            //anything else 
            throw new Exception("Unknown token in RPN: " + token);
        }

        if (exprStack.size() != 1) {
            throw new Exception("Invalid RPN expression, remaining stack size: " + exprStack.size());
        }

        return exprStack.pop();
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+") ||
               token.equals("-") ||
               token.equals("*") ||
               token.equals("/") ||
               token.equals("^");
    }

    private BinaryOpExpr.Op toOp(String token) {
        switch (token) {
            case "+": return BinaryOpExpr.Op.ADD;
            case "-": return BinaryOpExpr.Op.SUB;
            case "*": return BinaryOpExpr.Op.MUL;
            case "/": return BinaryOpExpr.Op.DIV;
            case "^": return BinaryOpExpr.Op.POW;
            default:
                throw new IllegalArgumentException("Unknown operator: " + token);
        }
    }
}
