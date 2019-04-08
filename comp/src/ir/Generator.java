package ir;

import front.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator{
    private static void println(String line) { System.out.println(line);}

    private static void println(int x) {println(String.valueOf(x)); }

    static int iRegs = 0;
    static int fRegs = 0;
    static List<String> commands = new ArrayList<>();

    private static int getIReg() {return iRegs++;}
    private static int getFReg() {return fRegs++;}

    private static Map<String, Integer> varRegs = new HashMap<>();

    private static int genExpression(Expression expr) {
        if (expr instanceof Expression.Operand) {
            Expression.Operand operand = (Expression.Operand) expr;
            Token value = operand.value;
            switch (value.getKind()) {
                case IDENT:
                    Token.Ident ident = (Token.Ident) value;
                    if (varRegs.containsKey(ident.word)) return varRegs.get(ident.word);
                    // add exception?
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
                case ASSIGN:
                    commands.add("IMOV " + res_2 + " " + res_1);
                    return iRegs;
                case EQ:
                    int eqRegs = getIReg();
                    commands.add("CMPEQ " + res_1 + " " + res_2 + " " + eqRegs);
                    return eqRegs;
                case NE:
                    int neReqs = getIReg();
                    commands.add("CMPNE " + res_1 + " " + res_2 + " " + neReqs);
                    return neReqs;
                case BG:
                    int bgReqs = getIReg();
                    commands.add("CMPBG " + res_1 + " " + res_2 + " " + bgReqs);
                    return bgReqs;
                case LS:
                    int lsReqs = getIReg();
                    commands.add("CMPLS " + res_1 + " " + res_2 + " " + lsReqs);
                    return lsReqs;
                case BGEQ:
                    int bgeqReqs = getIReg();
                    commands.add("CMPBE " + res_1 + " " + res_2 + " " + bgeqReqs);
                    return bgeqReqs;
                case LSEQ:
                    int lseqRegs = getIReg();
                    commands.add("CMPGE " + res_1 + " " + res_2 + " " + lseqRegs);
                    return lseqRegs;
                case AND:
                    int andRegs = getIReg();
                    commands.add("LAND " + res_1 + " " + res_2 + " " + andRegs);
                    return andRegs;
                case OR:
                    int orRegs = getIReg();
                    commands.add("LOR " + res_1 + " " + res_2 + " " + orRegs);
                    return orRegs;
                case NOT:
                    int notRegs = getIReg();
                    commands.add("LNOT " + res_1 + " " + res_2 + " " + notRegs);
                    return notRegs;
            }

        }
        return -1;
    }

    public static void generateBody(Operator[] body) {
        for (Operator operator: body) {
            if (operator instanceof Operator.Return) {
                Operator.Return ret = (Operator.Return) operator;
                int result = genExpression(ret.value);
                commands.add("IMOV " + result + " " + 0);
                commands.add("RET");
            } else if (operator instanceof Operator.SimpleExpression) {
                Operator.SimpleExpression expression = (Operator.SimpleExpression) operator;
                genExpression(expression.expression);
            } else if (operator instanceof Operator.Variable){
                Operator.Variable variable = (Operator.Variable) operator;
                switch (variable.type) {
                    case INT:
                        int iReg = getIReg();
                        varRegs.put(variable.name, iReg);
                        break;
                    case FLOAT:
                        int fReg = getFReg();
                        varRegs.put(variable.name, fReg);
                        break;
                }
            } else if (operator instanceof Operator.If) {
                Operator.If expression = (Operator.If) operator;
                int condition = genExpression(expression.condition);

                commands.add("IF " + condition + " " + (commands.size() + 2));
                int pos = commands.size();

                generateBody(expression.thenPart);

                if (expression.elsePart.length == 0) {
                    commands.add(pos, "GOTO " + (commands.size() + 1));
                } else {
                    commands.add(pos, "GOTO " + (commands.size() + 2));

                    pos = commands.size();
                    generateBody(expression.elsePart);
                    commands.add(pos, "GOTO " + (commands.size() + 1));
                }
            } else if (operator instanceof Operator.While) {
                Operator.While expression = (Operator.While) operator;
                int condition = genExpression(expression.condition);

                commands.add("IF " + condition + " " + (commands.size() + 2));
                int pos = commands.size();

                generateBody(expression.body);

                commands.add(pos, "GOTO " + (commands.size() + 2));
                commands.add("GOTO " + (pos-3));
            }
        }
    }

    public static void generateCode(IR ir, String name) {
        println(ir.functions.length);

        for (Function function : ir.functions) {
            for (Function.Parameter parameter : function.parameters) {
                switch (parameter.type) {
                    case INT:
                        varRegs.put(parameter.name, getIReg());
                        break;
                    case FLOAT:
                        varRegs.put(parameter.name, getFReg());
                        break;
                }
            }

            println(function.name);

            generateBody(function.body);

            println(iRegs);
            println(fRegs);
            println(commands.size());
            for (String command: commands) {
                println(command);
            }

            iRegs = 0;
            fRegs = 0;
            commands.clear();
            varRegs.clear();
        }
    }
}