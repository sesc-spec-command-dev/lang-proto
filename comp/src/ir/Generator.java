package ir;

import front.*;

import java.util.ArrayList;
import java.util.List;

public class Generator{
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