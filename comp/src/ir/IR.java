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
import front.Token.Ident;
import front.Token.IntLiteral;
import front.Token.FloatLiteral;
import front.Token.StrLiteral;
import java.util.ArrayList;

public class IR {
    public final Function[] functions;

    public IR(Function[] functions) {
        this.functions = functions;
    }
    
    public void printFunctions() { //? PrintStream param - System.out
    	System.out.println("Function list:");
    	System.out.println("------------------------------------------");
    	for(int i = 0; i < functions.length; i++) {
    		System.out.println("Function" + i + ":");
    		printFunction(functions[i]);
    	}
    }
    
    private static void printFunction(Function func) {
    	System.out.println(" -name" +" - " + func.name);
    	System.out.println(" -return type" + " - " + func.returnType);
    	System.out.println(" -parameter list:");
    	
    	if(func.parameters != null) {
    		for (int i = 0; i < func.parameters.length; i++) {
    			System.out.println("  [" + i + "] - (" + "type - " + func.parameters[i].type + ";"+"name - "+func.parameters[i].name+")");
    		}
    	}
    	else {
    		System.out.println("	|empty|");
    	}
    	
    	System.out.println("-function body:");
    	printOperators(func.body, 1);
    }
    
    private static void printOperators(ir.Operator[] opArr, int spaceNumber) {
    	if (opArr != null) {

    		if (opArr.length != 0) {
    			Object[] result;

				for (int i = 0; i < opArr.length; i++) {
					ir.Operator theOp = opArr[i];
					printSpaces(spaceNumber);
					System.out.print("[" + i + "] - ");             // printing index of the operator

					if (theOp instanceof If) {                         //"If case"
						If theIf = (If) theOp;

						System.out.println("If");
						printSpaces(spaceNumber + 7);
						System.out.print("condition - ");
						result = outputExpressionTree(theIf.condition);
						printExpressionTree(result);

						System.out.println();
						printSpaces(spaceNumber + 7);

						System.out.println("then part:");
						printOperators(theIf.thenPart, spaceNumber + 8);

						printSpaces(spaceNumber + 7);
						System.out.println("else part:");
						printOperators(theIf.elsePart, spaceNumber + 8);
					}
					if (theOp instanceof While) {                    //"while" case
						While theWhile = (While) theOp;

						System.out.println("While");
						printSpaces(spaceNumber + 7);
						System.out.print("condition - ");
						result = outputExpressionTree(theWhile.condition);
						printExpressionTree(result);

						System.out.println();
						printSpaces(spaceNumber + 7);

						System.out.println("body:");
						printOperators(theWhile.body, spaceNumber + 8);
					}

					if (theOp instanceof Return) {                    //"return" case
						Return ret = (Return) theOp;
						System.out.println("Return");
						printSpaces(spaceNumber + 7);
						System.out.print("value - ");
						result = outputExpressionTree(ret.value);
						printExpressionTree(result);

						System.out.println();
					}

					if (theOp instanceof SimpleExpression) {        //"SimpleExpression" case
						SimpleExpression expr = (SimpleExpression) theOp;

						System.out.println("SimpleExpression");
						printSpaces(spaceNumber + 7);
						System.out.print("expression - ");
						result = outputExpressionTree(expr.expression);
						printExpressionTree(result);

						System.out.println();
					}

					if (theOp instanceof Variable) {                //"Variable" case
						Variable variable = (Variable) theOp;

						System.out.println("Variable");
						printSpaces(spaceNumber + 7);
						System.out.println("Type - " + variable.type);
						printSpaces(spaceNumber + 7);
						System.out.println("Value - " + variable.name);
					}
				}
			}
			else {
				printSpaces(spaceNumber);
				System.out.println("	|empty|");
			}
    	}
    }

    private static void printExpressionTree(Object[] result) {
		for(int i = 0; i < result.length; i++) {
			System.out.print(result[i]);
		}
	}

    private static Object[] outputExpressionTree(Expression expr) {
		ArrayList<String> result = new ArrayList<>();
		String str;

		if(expr instanceof Operation) {
			Operation theOper = (Operation) expr;

			if(theOper.left != null) {
				Object[] temp = outputExpressionTree(theOper.left);

				for(int i = 0;  i < temp.length; i++) {
					result.add(" " + temp[i]);
				}
			}
			str = getExprValue(expr);
			result.add(str);

			if(theOper.right != null) {
				Object[] temp = outputExpressionTree(theOper.right);

				for(int i = 0; i < temp.length; i++) {
					result.add(" " + temp[i]);
				}
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
			Operand op = (Operand) expr;
			Token val = op.value;

			switch(val.getKind()) {
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
					throw new ParserException("Incorrect operand value in expression",val.position);
			}
		}
	}
    
    private static void printSpaces(int spaceNumber) {
    	for(int i = 0; i < spaceNumber; i++) {
    		System.out.print(" ");
    	}
    }
}
