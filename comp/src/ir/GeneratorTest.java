package ir;

import front.Position;
import front.Token;

public class GeneratorTest {

    /*
        tests:
        1)
            int foo() {
                return 42;
            }
        2)
            int foo() {
                return 42 + 37;
            }
        3)
            int foo() {
                return 5 / (3 * 24) - 8;
            }
        4)
            int foo() {
                int x;
                x = 42;
                return x;
            }
        5)
            int foo() {
                int x;
                x = 42;
                int y;
                y = 37;
                return x + y;
            }
    */

    public static void main(String[] args) {
        Position position = new Position(0, 0);


        Token literal42 = Token.make(Token.Kind.INT_LITERAL, position, 42); // 42
        Expression returnValue_1 = new Expression.Operand(literal42);

        Token literal37 = Token.make(Token.Kind.INT_LITERAL, position, 37); // 37
        Expression returnValue_2 = new Expression.Operand(literal37);

        Token literal3 = Token.make(Token.Kind.INT_LITERAL, position, 3); // 3
        Expression returnValue_3 = new Expression.Operand(literal3);

        Token literal5 = Token.make(Token.Kind.INT_LITERAL, position, 5); // 5
        Expression returnValue_4 = new Expression.Operand(literal5);

        Token literal24 = Token.make(Token.Kind.INT_LITERAL, position, 24); // 24
        Expression returnValue_5 = new Expression.Operand(literal24);

        Token literal8 = Token.make(Token.Kind.INT_LITERAL, position, 8); // 8
        Expression returnValue_6 = new Expression.Operand(literal8);

        Token modOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.MOD);
        Expression mod = new Expression.Operation((Token.Operator)modOp, returnValue_5, returnValue_1 ); // 7 mod Expression div

        Token addOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.ADD);
        Expression add = new Expression.Operation((Token.Operator)addOp, returnValue_1, returnValue_2); // 42 + 37

        Token mulOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.MUL);
        Expression mul = new Expression.Operation((Token.Operator)mulOp, returnValue_3, returnValue_5); // 3 * 24

        Token divOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.DIV);
        Expression div = new Expression.Operation((Token.Operator)divOp, returnValue_4, mul); // 5 / (3 * 24)

        Token subOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.SUB);
        Expression sub = new Expression.Operation((Token.Operator)subOp, div, returnValue_6); // 5 / (3 * 24) - 8

        Operator[] body_1 = new Operator[1];
        body_1[0] = new Operator.Return(returnValue_1);

        Operator[] body_2 = new Operator[1];
        body_2[0] = new Operator.Return(add);

        Operator[] body_3 = new Operator[1];
        body_3[0] = new Operator.Return(sub);

        Function.Parameter[] parameters = new Function.Parameter[0];

        Function[] functions = new Function[3];
        functions[0] = new Function(Type.INT, "foo_1", parameters, body_1);
        functions[1] = new Function(Type.INT, "foo_2", parameters, body_2);
        functions[2] = new Function(Type.INT, "foo_3", parameters, body_3);

        IR ir = new IR(functions);

        Generator.generateCode(ir, "a.txt");
    }
}
