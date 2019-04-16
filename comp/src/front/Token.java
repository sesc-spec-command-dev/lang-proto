package front;

import Parser.ParserException;

public abstract class Token {

    public static Token make(Kind kind, Position position, Object value) {
        switch (kind) {
            case KEYWORD: return new KeyWord(position, (KeyWords)value);
            case IDENT: return new Ident(position, (String)value);
            case INT_LITERAL: return new IntLiteral(position, (Integer)value);
            case FLOAT_LITERAL: return new FloatLiteral(position, (Float)value);
            case STR_LITERAL: return new StrLiteral(position, (String)value);
            case OPERATOR: return new Operator(position, (Operators) value);
            default: assert(false); return null;
        }
    }

    public final Position position;

    Token(Position position) {
        this.position = position;
    }

    ///////////////////////////////////////////////////////

    public enum Kind {
        KEYWORD,
        IDENT,
        INT_LITERAL,
        FLOAT_LITERAL,
        STR_LITERAL,
        OPERATOR
    }

    public abstract Kind getKind();

    ///////////////////////////////////////////////////////

    public enum KeyWords {
        VOID,
        INT,
        FLOAT,
        IF,
        ELSE,
        WHILE,
        RETURN,
        WRITE;

        public String getText() {
            return name().toLowerCase();
        }
    }

    public static class KeyWord extends Token {

        public final KeyWords word;

        public KeyWord(Position position, KeyWords word) {
            super(position);
            this.word = word;
        }

        @Override
        public Kind getKind() {
            return Kind.KEYWORD;
        }
    }

    ///////////////////////////////////////////////////////

    public static class Ident extends Token {

        public final String word;

        public Ident(Position position, String word) {
            super(position);
            this.word = word;
        }

        @Override
        public Kind getKind() {
            return Kind.IDENT;
        }
    }

    ///////////////////////////////////////////////////////

    public static class IntLiteral extends Token {

        public final int value;

        public IntLiteral(Position position, int value) {
            super(position);
            this.value = value;
        }

        @Override
        public Kind getKind() {
            return Kind.INT_LITERAL;
        }
    }

    ///////////////////////////////////////////////////////

    public static class FloatLiteral extends Token {

        public final float value;

        public FloatLiteral(Position position, float value) {
            super(position);
            this.value = value;
        }

        @Override
        public Kind getKind() {
            return Kind.FLOAT_LITERAL;
        }
    }

    ///////////////////////////////////////////////////////

    public static class StrLiteral extends Token {

        public final String value;

        public StrLiteral(Position position, String value) {
            super(position);
            this.value = value;
        }

        @Override
        public Kind getKind() {
            return Kind.STR_LITERAL;
        }
    }

    ///////////////////////////////////////////////////////

    public enum Operators {
        ADD("+"), // +
        SUB("-"), // -
        MUL("*"), // *
        DIV("/"), // /
        MOD("%"), // %
        ASSIGN("="), // =
        EQ("=="), // ==
        NE("!="), // !=
        BG(">"), // >
        LS("<"), // <
        BGEQ(">="), // >=
        LSEQ("<="), // <=
        AND("&&"), // &&
        OR("||"), // ||
        NOT("!"), // !
        OPEN_CURLY_BRACE("{"), // {
        CLOSE_CURLY_BRACE("}"), // }
        OPEN_PARENTHESIS("("), // (
        CLOSE_PARENTHESIS(")"), // )
        OPEN_BRACKET("["), // [
        CLOSE_BRACKET("]"), // ]
        COLON(":"), // :
        SEMICOLON(";"), // ;
        COMMA(","); // ,

        public final String value;
      
        //? int priority(), with check of correct operator
        public int priority(Operator op) {
        	switch(op.operator.value) {
        		case "=":
        			return -1;
        		case "&&":
        		case "||":
        		case "!":
        			return 0;
        		case "==":
        		case "!=":
        		case ">=":
        		case "<=":
        		case ">":
        		case "<":
        			return 1;
        		case "+":
        		case "-":
        			return 2;
        		case "*":
        		case "/":
        			return 3;
        		default:
        			throw new ParserException("Incorrect operation in expression", op.position);
        	}
        }
        
        Operators(String value) {
            this.value = value;
        }
    }

    public static class Operator extends Token {

        public final Operators operator;

        public Operator(Position position, Operators operator) {
            super(position);
            this.operator = operator;
        }

        @Override
        public Kind getKind() {
            return Kind.OPERATOR;
        }
    }
}
