package ir;

import front.*;

import java.util.ArrayList;
import java.util.List;

public class Generator{

    public static void main(String [] args) {
        Position position = new Position(0, 0);


        Token literal42 = Token.make(Token.Kind.INT_LITERAL, position, 42);
        Expression returnValue_1 = new Expression.Operand(literal42);

        Token literal37 = Token.make(Token.Kind.INT_LITERAL, position, 37);
        Expression returnValue_2 = new Expression.Operand(literal37);

        Token literal3 = Token.make(Token.Kind.INT_LITERAL, position, 3);
        Expression returnValue_3 = new Expression.Operand(literal3);

        Token literal5 = Token.make(Token.Kind.INT_LITERAL, position, 5);
        Expression returnValue_4 = new Expression.Operand(literal5);

        Token literal7 = Token.make(Token.Kind.INT_LITERAL, position, 7);
        Expression returnValue_5 = new Expression.Operand(literal7);



        Token modOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.MOD);
        Expression mod = new Expression.Operation((Token.Operator)modOp, returnValue_5, returnValue_1 ); // 7 mod Expression div

        Token addOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.ADD);
        Expression add = new Expression.Operation((Token.Operator)addOp, returnValue_1, returnValue_2); //

        Token mulOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.MUL);
        Expression mul = new Expression.Operation((Token.Operator)mulOp, returnValue_1, returnValue_2); // 42 - 37

        Token divOp = Token.make(Token.Kind.OPERATOR, position, Token.Operators.DIV);
        Expression div = new Expression.Operation((Token.Operator)divOp, returnValue_4, returnValue_3); // 5/3



        Operator[] body = new Operator[2];
        body[0] = new Operator.Return(mul); // return 42 - 37
        body[1] = new Operator.Return(div); // return 5/3

        Function.Parameter[] parameters = new Function.Parameter[0];


        Function[] functions = new Function[1];
        functions[0] = new Function(Type.INT, "main", parameters, body);
        IR ir = new IR(functions);

        generateCode(ir, "a.txt");
    }

    private static void println(String line) { System.out.println(line);}

    private static void println(int x) {println(String.valueOf(x)); }

    static int iRegs = 0;
    static int fRegs = 0;
    static List<String> commands = new ArrayList<>();

    private static int getIReg() {return iRegs++;}
    private static int getFReg() {return fRegs++;}


    private static int genExpression(Expression expr) {
        if (expr instanceof Expression.Operand) {
            Expression.Operand operand = (Expression.Operand) expr;
            Token value = operand.value;
            switch (value.getKind()) {
                case IDENT:
                    assert false;
                    break;
                case INT_LITERAL:
                    Token.IntLiteral intLiteral = (Token.IntLiteral) value;
                    int iReg = getIReg();
                    commands.add("ILOAD " + intLiteral.value + " " + iReg);
                return iReg;
                case FLOAT_LITERAL:
                    Token.IntLiteral floatLiteral = (Token.IntLiteral) value;
                    int fReg = getIReg();
                    commands.add("ILOAD " + floatLiteral.value + " " + fReg);
                    return fReg;
                case STR_LITERAL:
                    assert false;
                    break;
                default:
                    assert false;
                    break;
            }
        } else {
            Expression.Operation operation = (Expression.Operation) expr;
            int res_1 = genExpression(operation.left);
            int res_2 = genExpression(operation.right);
            Token.Operators operator_1  = operation.operation.operator;

            switch (operator_1) {
                case ADD:
                    int iReg = getIReg();
                    commands.add("IADD " + res_1 + " " + res_2 + " " + iReg);
                    return iReg;
                case SUB:
                    int sRegs = getIReg();
                    commands.add("ISUB " + res_1 + " " + res_2 + " " + sRegs);
                    return sRegs;
                case MUL:
                    int mRegs = getIReg();
                    commands.add("IMUL " + res_1 + " " + res_2 +  " " + mRegs);
                    return mRegs;
                case DIV:
                    int dRegs = getIReg();
                    commands.add("IDIV " + res_1 + " " + res_2 +  " " + dRegs);
                    return dRegs;
                case MOD:
                    int moRegs = getIReg();
                    commands.add("IMOD " + res_1 + " " + res_2 +  " " + moRegs);
                    return moRegs;
            }

        }
        return -1;
    }

    public static void generateCode(IR ir, String name) {
        println(ir.functions.length);

        for (Function function : ir.functions) {
            println(function.name);

            for (Operator operator: function.body) {
                if (operator instanceof Operator.Return) {
                    Operator.Return ret = (Operator.Return) operator;
                    int result = genExpression(ret.value);
                    commands.add("IMOV " + result + " 0");
                    commands.add("RET");
                } else if (operator instanceof Operator.SimpleExpression) {
                    Operator.SimpleExpression expression = (Operator.SimpleExpression) operator;
                    genExpression(expression.expression);
                    //commands.add("IADD " + result + )
                } else{
                    assert false;
                }
            }
            // function.body => commands;

            println(iRegs);
            println(fRegs);
            println(commands.size());
            for (String command: commands) {
                println(command);
            }
        }
    }
}