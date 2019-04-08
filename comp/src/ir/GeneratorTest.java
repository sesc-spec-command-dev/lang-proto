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
        6)
            int foo() {
                int x;
                x = 12;

                if (x == 13) {
                    x = 11;
                }

                return x;
            }
        7)
            int foo() {
                int x;
                x = 11;

                while (x > 5) {
                    x = x - 3;
                }

                return x;
            }
        8)
            int foo() {
                int x;
                x = 12;

                if (x == 13) {
                    x = 11;
                } else {
                    x = 10;
                }

                return x;
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

        Token literal12 = Token.make(Token.Kind.INT_LITERAL, position, 12); // 12
        Expression returnValue_7 = new Expression.Operand(literal12);

        Token literal11 = Token.make(Token.Kind.INT_LITERAL, position, 11); // 11
        Expression returnValue_8 = new Expression.Operand(literal11);

        Token literal13 = Token.make(Token.Kind.INT_LITERAL, position, 13); // 13
        Expression returnValue_9 = new Expression.Operand(literal13);

        Token literal10 = Token.make(Token.Kind.INT_LITERAL, position, 10); // 13
        Expression returnValue_10 = new Expression.Operand(literal10);

        Operator.Variable variableX = new Operator.Variable(Type.INT, "x"); // int x

        Operator.Variable variableY = new Operator.Variable(Type.INT, "y"); // int y

        Token identX = Token.make(Token.Kind.IDENT, position, "x"); // x
        Expression identXOperand = new Expression.Operand(identX);

        Token assignXOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.ASSIGN);
        Expression assignXto42 = new Expression.Operation((Token.Operator)assignXOp, identXOperand, returnValue_1); // x = 42

        Expression assignXto12 = new Expression.Operation((Token.Operator)assignXOp, identXOperand, returnValue_7); // x = 12

        Expression assignXto11 = new Expression.Operation((Token.Operator)assignXOp, identXOperand, returnValue_8); // x = 11

        Expression assignXto10 = new Expression.Operation((Token.Operator)assignXOp, identXOperand, returnValue_10); // x = 10

        Token isXeqTo11Op = Token.make(Token.Kind.OPERATOR, position, Token.Operators.EQ);
        Expression isXeqto11 = new Expression.Operation((Token.Operator) isXeqTo11Op, identXOperand, returnValue_9); // x == 13

        Token isXbg10Op = Token.make(Token.Kind.OPERATOR, position, Token.Operators.BG);
        Expression isXbg10 = new Expression.Operation((Token.Operator) isXbg10Op, identXOperand, returnValue_4);

        Token identY = Token.make(Token.Kind.IDENT, position, "y");
        Expression identYOperand = new Expression.Operand(identY); // y

        Token assignYOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.ASSIGN);
        Expression assignYto37 = new Expression.Operation((Token.Operator)assignYOp, identYOperand, returnValue_2); // y = 37

        Token addXtoYOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.ADD);
        Expression addXtoY = new Expression.Operation((Token.Operator)addXtoYOp, identXOperand, identYOperand);

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

        Expression subXand3 = new Expression.Operation((Token.Operator) subOp, identXOperand, returnValue_3);
        Expression assignXtoIncr = new Expression.Operation((Token.Operator) assignXOp, identXOperand, subXand3);

        Operator[] body_1 = new Operator[1];
        body_1[0] = new Operator.Return(returnValue_1);

        Operator[] body_2 = new Operator[1];
        body_2[0] = new Operator.Return(add);

        Operator[] body_3 = new Operator[1];
        body_3[0] = new Operator.Return(sub);

        Operator[] body_4 = new Operator[3];
        body_4[0] = variableX;
        body_4[1] = new Operator.SimpleExpression(assignXto42);
        body_4[2] = new Operator.Return(identXOperand);

        Operator[] body_5 = new Operator[5];
        body_5[0] = variableX;
        body_5[1] = new Operator.SimpleExpression(assignXto42);
        body_5[2] = variableY;
        body_5[3] = new Operator.SimpleExpression(assignYto37);
        body_5[4] = new Operator.Return(addXtoY);

        Operator[] body_6 = new Operator[4];
        body_6[0] = variableX;
        body_6[1] = new Operator.SimpleExpression(assignXto12);

        Operator[] body_if_1 = new Operator[1];
        body_if_1[0] = new Operator.SimpleExpression(assignXto11);

        Operator.If if_1 = new Operator.If(new Operator.SimpleExpression(isXeqto11).expression, body_if_1, new Operator[0]);

        body_6[2] = if_1;
        body_6[3] = new Operator.Return(identXOperand);

        Operator[] body_7 = new Operator[4];
        body_7[0] = variableX;
        body_7[1] = new Operator.SimpleExpression(assignXto11);

        Operator[] body_while_1 = new Operator[1];
        body_while_1[0] = new Operator.SimpleExpression(assignXtoIncr);

        Operator.While while_1 = new Operator.While(new Operator.SimpleExpression(isXbg10).expression, body_while_1);

        body_7[2] = while_1;
        body_7[3] = new Operator.Return(identXOperand);

        Operator[] body_8 = new Operator[10];
        body_8[0] = variableX;
        body_8[1] = new Operator.SimpleExpression(assignXto12);

        Operator[] elsePart = new Operator[1];
        elsePart[0] = new Operator.SimpleExpression(assignXto10);

        Operator.If if_2 = new Operator.If(new Operator.SimpleExpression(isXeqto11).expression, body_if_1, elsePart);

        body_8[2] = if_2;
        body_8[3] = new Operator.Return(identXOperand);

        Function.Parameter[] parameters = new Function.Parameter[0];

        Function[] functions = new Function[8];
        functions[0] = new Function(Type.INT, "foo_1", parameters, body_1);
        functions[1] = new Function(Type.INT, "foo_2", parameters, body_2);
        functions[2] = new Function(Type.INT, "foo_3", parameters, body_3);
        functions[3] = new Function(Type.INT, "foo_4", parameters, body_4);
        functions[4] = new Function(Type.INT, "foo_5", parameters, body_5);
        functions[5] = new Function(Type.INT, "foo_6", parameters, body_6);
        functions[6] = new Function(Type.INT, "foo_7", parameters, body_7);
        functions[7] = new Function(Type.INT, "foo_8", parameters, body_8);

        IR ir = new IR(functions);

        Generator.generateCode(ir, "a.txt");
    }
}
