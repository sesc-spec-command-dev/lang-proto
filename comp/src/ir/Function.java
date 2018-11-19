package ir;

public class Function {

    public static class Parameter {
        public final String name;
        public final Type type;

        public Parameter(String name, Type type) {
            this.name = name;
            this.type = type;
        }
    }

    public final Type returnType;
    public final String name;
    public final Parameter[] parameters;
    public final Operator[] body;

    public Function(Type returnType, String name, Parameter[] parameters, Operator[] body) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
}
