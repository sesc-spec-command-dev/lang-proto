package ir;

import front.Position;
import front.Token;

public abstract class Expression {
    public abstract Position position();

    public static class Operand extends Expression {
        public final Token value;

        public Operand(Token value) {
            this.value = value;
        }

        @Override
        public Position position() {
            return value.position;
        }
    }

    public static class Operation extends Expression {
        public final Token.Operator operation;
        public final Expression left;
        public final Expression right;

        public Operation(Token.Operator operation, Expression left, Expression right) {
            this.operation = operation;
            this.left = left;
            this.right = right;
        }

        @Override
        public Position position() {
            return operation.position;
        }
    }

    public static class FunctionCall extends Expression {
        public Token.Ident link;
        public Expression[] parameterList;

        public FunctionCall(Token.Ident link, Expression[] parameters) {
            this.link = link;
            this.parameterList = parameters;
        }

        @Override
        public Position position() {return link.position; }
    }
}
