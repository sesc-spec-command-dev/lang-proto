package ir;

import Parser.ParserException;
import front.Token;
import ir.Operator.If;
import ir.Operator.Return;
import ir.Operator.SimpleExpression;
import ir.Operator.Variable;
import ir.Operator.While;
import ir.Expression.Operation;
import ir.Expression.Operand;
import ir.Expression.FunctionCall;
import front.Token.Ident;
import front.Token.IntLiteral;
import front.Token.FloatLiteral;
import front.Token.StrLiteral;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

public class IR {
    public final Function[] functions;
    static int irPrintCount = 21;
	static PrintWriter pw;
    public IR(Function[] functions) {
        this.functions = functions;
    }
    
    private void printFunctions() { //? PrintStream param - System.out
    	pw.println("Function list:");
    	pw.println("------------------------------------------");
    	for(int i = 0; i < functions.length; i++) {
    		pw.println("Function" + i + ":");
    		printFunction(functions[i]);
    	}
    }

    public void printIR(String message) throws FileNotFoundException {
		String str = Integer.toString(irPrintCount);
		/*
		String[] outputStr = {"0", "0", "0"};
		char[] arr = str.toCharArray();

		for(int i = 0; i < arr.length; i++) {
			outputStr[2 - i] = "";
			outputStr[2 - i] += arr[arr.length - 1 - i];
		}
		String out = Arrays.toString(outputStr);*/

		String str2;
		if (irPrintCount < 10) {
			str2 = "00" + str;
		} else if (irPrintCount < 100) {
			str2 = "0" + str;
		} else {
			str2 = str;
		}

		File irOutput = new File(str2 + "_" + message);

		pw = new PrintWriter(irOutput);
		printFunctions();
		pw.close();
	}
    
    private static void printFunction(Function func) {
    	pw.println(" -name" +" - " + func.name);
    	pw.println(" -return type" + " - " + func.returnType);
    	pw.println(" -parameter list:");
    	
    	if(func.parameters != null) {
    		for (int i = 0; i < func.parameters.length; i++) {
    			pw.println("  [" + i + "] - (" + "type - " + func.parameters[i].type + ";"+"name - "+func.parameters[i].name+")");
    		}
    	}
    	else {
    		pw.println("	|empty|");
    	}
    	
    	pw.println("-function body:");
    	printOperators(func.body, 1);
    }
    
    private static void printOperators(ir.Operator[] opArr, int spaceNumber) {
    	if (opArr != null) {

    		if (opArr.length != 0) {
    			Object[] result;

				for (int i = 0; i < opArr.length; i++) {
					ir.Operator theOp = opArr[i];
					printSpaces(spaceNumber);
					pw.print("[" + i + "] - ");             // printing index of the operator

					if (theOp instanceof If) {                         //"If" case
						If theIf = (If) theOp;

						pw.println("If");
						printSpaces(spaceNumber + 7);
						pw.print("condition - ");
						result = outputExpressionTree(theIf.condition);
						printExpressionTree(result);

						pw.println();
						printSpaces(spaceNumber + 7);

						pw.println("then part:");
						printOperators(theIf.thenPart, spaceNumber + 8);

						printSpaces(spaceNumber + 7);
						pw.println("else part:");
						printOperators(theIf.elsePart, spaceNumber + 8);
					}
					if (theOp instanceof While) {                    //"while" case
						While theWhile = (While) theOp;

						pw.println("While");
						printSpaces(spaceNumber + 7);
						pw.print("condition - ");
						result = outputExpressionTree(theWhile.condition);
						printExpressionTree(result);

						pw.println();
						printSpaces(spaceNumber + 7);

						pw.println("body:");
						printOperators(theWhile.body, spaceNumber + 8);
					}

					if (theOp instanceof Return) {                    //"return" case
						Return ret = (Return) theOp;
						pw.println("Return");
						printSpaces(spaceNumber + 7);
						pw.print("value - ");
						result = outputExpressionTree(ret.value);
						printExpressionTree(result);

						pw.println();
					}

					if (theOp instanceof SimpleExpression) {        //"SimpleExpression" case
						SimpleExpression expr = (SimpleExpression) theOp;

						if(expr.expression instanceof FunctionCall) {
							FunctionCall fCall = (FunctionCall) expr.expression;

							pw.println("FunctionCalll");
							printSpaces(spaceNumber + 7);
							pw.println("name - " + fCall.link.word);
							printSpaces(spaceNumber + 7);
							pw.println("parameterArr:");


						}
						else {
							pw.println("SimpleExpression");
							printSpaces(spaceNumber + 7);
							pw.print("expression - ");
							result = outputExpressionTree(expr.expression);
							printExpressionTree(result);

							pw.println();
						}
					}

					if (theOp instanceof Variable) {                //"Variable" case
						Variable variable = (Variable) theOp;

						pw.println("Variable");
						printSpaces(spaceNumber + 7);
						pw.println("Type - " + variable.type);
						printSpaces(spaceNumber + 7);
						pw.println("Value - " + variable.name);
					}
				}
			}
			else {
				printSpaces(spaceNumber);
				pw.println("	|empty|");
			}
    	}
    }

    private static void printExpressionTree(Object[] result) {
		for(int i = 0; i < result.length; i++) {
			pw.print(result[i]);
		}
	}

    private static Object[] outputExpressionTree(Expression expr) {
		ArrayList<String> result = new ArrayList<>();
		String str;

		if(expr instanceof Operation) {
			Operation theOper = (Operation) expr;

			if(theOper.left != null) {
				Object[] temp = outputExpressionTree(theOper.left);
				result.add("(");
				for(int i = 0;  i < temp.length; i++) {
					result.add(temp[i].toString());
				}
			}
			str = getExprValue(expr);
			result.add(" " + str + " ");

			if(theOper.right != null) {
				Object[] temp = outputExpressionTree(theOper.right);

				for(int i = 0; i < temp.length; i++) {
					result.add(temp[i].toString());
				}
				result.add(")");
			}
		}
		else {
			str = getExprValue(expr);
			result.add(str);
		}
		return result.toArray();
    }

	public static String getExprValue(Expression expr) {
    	if (expr instanceof  Operation) {
    		Operation theOper = (Operation) expr;
    		return theOper.operation.operator.value;
		}
		else {
			if(expr instanceof Operand) {
				Operand op = (Operand) expr;
				Token val = op.value;

				switch (val.getKind()) {
					case IDENT:
						Ident ident = (Ident) val;
						return ident.word;

					case INT_LITERAL:
						IntLiteral intLit = (IntLiteral) val;
						return Integer.toString(intLit.value);

					case FLOAT_LITERAL:
						FloatLiteral fLit = (FloatLiteral) val;
						return Float.toString(fLit.value);

					case STR_LITERAL:
						StrLiteral strLit = (StrLiteral) val;
						return strLit.value;
					default:
						throw new ParserException("Incorrect operand value in expression", val.position);
				}
			}
			else {
				FunctionCall fCall = (FunctionCall) expr;
				return fCall.link.word + "()";
			}
		}
	}
    
    private static void printSpaces(int spaceNumber) {
    	for(int i = 0; i < spaceNumber; i++) {
    		pw.print(" ");
    	}
    }
}
