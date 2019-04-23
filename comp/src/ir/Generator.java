package ir;

import front.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Generator{
    private static void println(String line, PrintWriter pw) {pw.println(line);}

    private static void println(int x, PrintWriter pw) {println(String.valueOf(x), pw); }

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
                    break;
                case INT_LITERAL:
                    Token.IntLiteral intLiteral = (Token.IntLiteral) value;
                    int iReg = getIReg();
                    commands.add("ILOAD " + intLiteral.value + " " + iReg);
                return iReg;
                case FLOAT_LITERAL:
                    Token.FloatLiteral floatLiteral = (Token.FloatLiteral) value;
                    int fReg = getIReg();
                    commands.add("FLOAD " + floatLiteral.value + " " + fReg);
                    return fReg;
                case STR_LITERAL:
                    assert false;
                    break;
                default:
                    assert false;
                    break;
            }
        } else if (expr instanceof Expression.Operation){
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
        } else if (expr instanceof Expression.FunctionCall) {
            int callRegs = getIReg();

            Expression.FunctionCall functionCall = (Expression.FunctionCall) expr;
            List<Integer> res = new ArrayList<>();

            for (Expression expression : functionCall.parameterList) {
                res.add(genExpression(expression));
            }

            StringBuilder callString = new StringBuilder("CALL ");
            callString.append(functionCall.link.word).append(" ");

            for (int r : res) {
                callString.append(r).append(" ");
            }

            callString.append(callRegs);
            commands.add(callString.toString());

            return callRegs;
        }
        return -1;
    }

    private static void generateBody(Operator[] body) {
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

                if (expression.elsePart == null) {
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
            } else if (operator instanceof Operator.Write) {
                Operator.Write write = (Operator.Write) operator;

                if(write.writeExpression instanceof Expression.Operation || write.writeExpression instanceof Expression.FunctionCall) {
                    int condition = genExpression(write.writeExpression);
                    commands.add("WRITE_INT " + condition);
                } else if (write.writeExpression instanceof Expression.Operand) {
                    Expression.Operand theOp = (Expression.Operand) write.writeExpression;

                    if(theOp.value.getKind() == Token.Kind.STR_LITERAL) {
                        Token.StrLiteral strLit = (Token.StrLiteral) theOp.value;
                        commands.add("WRITE_STR " + strLit.value.substring(1, strLit.value.length() - 1));
                    }
                    else {
                        int condition = genExpression(write.writeExpression);
                        commands.add("WRITE_INT " + condition);
                    }
                }
            }
        }
    }

    public static void generateCode(IR ir, String name) throws FileNotFoundException {
        File outputFile = new File("..\\Bytecode.txt");
        PrintWriter pw = new PrintWriter(outputFile);

        println(ir.functions.length, pw);

        for (Function function : ir.functions) {
            if (function.parameters != null) {
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
            }
            println(function.name, pw);
            generateBody(function.body);
            println(iRegs, pw);
            println(fRegs, pw);
            println(commands.size(), pw);
            for (String command: commands) {
                println(command, pw);
            }

            iRegs = 0;
            fRegs = 0;
            commands.clear();
            varRegs.clear();
            pw.close();
        }
    }
}