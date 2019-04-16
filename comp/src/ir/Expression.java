package ir;

import front.Position;
import front.Token;
import front.Token.Ident;

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
        public Expression left;
        public Expression right;

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
        public final Ident link;
        public Expression[] parameterList;

        public FunctionCall(Ident link, Expression[] pList) {
            this.link = link;
            this.parameterList = pList;
        }

        @Override
        public Position position() {return link.position;}
    }
}
