package ir;

import front.Token;

public abstract class Expression {

    public static class Operand extends Expression {
        public final Token value;

        public Operand(Token value) {
            this.value = value;
        }
    }

    public static class Operation extends Expression {				//operation class
        public final Token.Operator operation;
        public final Expression left;								//link on left operand/operation
        public final Expression right;								//link on right operand/operation

        public Operation(Token.Operator operation, Expression left, Expression right) {
            this.operation = operation;
            this.left = left;
            this.right = right;
        }
    }

}
