package ir;

public abstract class Operator {

    public static class If extends Operator {
        public Expression condition;
        public final Operator[] thenPart;
        public final Operator[] elsePart;

        public If(Expression condition, Operator[] thenPart, Operator[] elsePart) {
            this.condition = condition;
            this.thenPart = thenPart;
            this.elsePart = elsePart;
        }
    }

    public static class While extends Operator {
        public Expression condition;
        public final Operator[] body;
        
        public While(Expression condition, Operator[] body) {
            this.condition = condition;
            this.body = body;
        }
    }

    public static class Return extends Operator {
        public Expression value;

        public Return(Expression value) {
            this.value = value;
        }
    }

    public static class Variable extends Operator {
        public final Type type;
        public final String name;

        public Variable(Type type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    public static class SimpleExpression extends Operator {
        public Expression expression;

        public SimpleExpression(Expression expression) {
            this.expression = expression;
        }
    }

}
