package ir;

import ir.Operator.SimpleExpression;
import ir.Operator.If;
import ir.Operator.While;
import ir.Operator.Return;
import ir.Expression.Operation;
import ir.Expression.Operand;
import front.Token;
import front.Token.IntLiteral;
import front.Token.FloatLiteral;

public class Optimizer {
    IR inputIR;

    public Optimizer(IR ir) {           //constructor
        inputIR = ir;
    }

    private void optimize() {           //method includes all optimization phases
        for(int i = 0; i < inputIR.functions.length; i++) {
            constExpressionEvaluation(inputIR.functions[i].body);
        }

    }

    private void constExpressionEvaluation(Operator[]  opArr) {
        if (opArr != null) {
            if (opArr.length != 0) {

                for (int i = 0; i < opArr.length; i++) {

                    Operator op = opArr[i];

                    if (op instanceof If) {
                        If theIf = (If) op;
                        theIf.condition = dfs(theIf.condition);

                        constExpressionEvaluation(theIf.thenPart);
                        constExpressionEvaluation(theIf.elsePart);
                        op = theIf;                                     //refactor operator
                    }
                    if (op instanceof While) {
                        While theWhile = (While) op;
                        theWhile.condition = dfs(theWhile.condition);
                        constExpressionEvaluation(theWhile.body);
                        op = theWhile;                                  //refactor operator
                    }
                    if (op instanceof Return) {
                        Return ret = (Return) op;
                        ret.value = dfs(ret.value);
                        op = ret;                                       //refactor operator
                    }
                    if (op instanceof SimpleExpression) {
                        SimpleExpression expr = (SimpleExpression) op ;
                        expr.expression = dfs(expr.expression);
                        op = opArr[i];                                  //refactor operator
                    }
                    opArr[i] = op;                                   //update operator
                }
            }
        }
    }

    private Expression dfs(Expression exprTree) {
        if(exprTree instanceof Operation) {

            Operation operation = (Operation) exprTree;

            operation.left = dfs(operation.left);
            operation.right = dfs(operation.right);

            if (operation.left instanceof Operand && operation.right instanceof Operand) {
                Operand left = (Operand) operation.left;
                Operand right = (Operand) operation.right;

                if (left.value.getKind() == Token.Kind.INT_LITERAL && right.value.getKind() == Token.Kind.INT_LITERAL) {
                    exprTree = getConstValue(operation, left, right);

                } else if (left.value.getKind() == Token.Kind.FLOAT_LITERAL && right.value.getKind() == Token.Kind.FLOAT_LITERAL) {
                    exprTree = getConstValue(operation, left, right);
                }
            }
        }
        return exprTree;
    }

    private Operand getConstValue(Operation oper, Operand left, Operand right) {
        Token.Operator op = (Token.Operator) oper.operation;        //casting without exception(possible exception throws in AST building)
        if(left.value.getKind() == Token.Kind.INT_LITERAL) {
            IntLiteral lInt = (IntLiteral) left.value;
            IntLiteral rInt = (IntLiteral) right.value;
        }
        else {
            FloatLiteral lFloat = (FloatLiteral) left.value;
            FloatLiteral rFloat = (FloatLiteral) right.value;
        }
        switch (op.operator.value) {
            case "+":

        }
        return null;
    }
}
