package ir;

import ir.Operator.If;
import ir.Operator.Return;
import ir.Operator.SimpleExpression;
import ir.Operator.Variable;
import ir.Operator.While;

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
    		
    		for (int i = 0; i < opArr.length; i++) {
    			ir.Operator theOp = opArr[i];
    			printSpaces(spaceNumber);
    			System.out.print("[" + i + "] - ");			 // printing index of the operator
    			
    			if (theOp instanceof If) {						 //"If case"
    				If theIf = (If) theOp;
    				
    				System.out.println("If");
    				printSpaces(spaceNumber + 7);
    				System.out.print("condition - ");
    				printExpressionTree(theIf.condition);
    				
    				System.out.println();
    				printSpaces(spaceNumber + 7);
    				
    				System.out.println("then part:");
    				printOperators(theIf.thenPart, spaceNumber + 8);
    				
    				printSpaces(spaceNumber + 7);
    				System.out.println("else part:");
    				printOperators(theIf.elsePart, spaceNumber + 8);
    			}
    			if (theOp instanceof While) {					//"while" case
    				While theWhile = (While) theOp;
    				
    				System.out.println("While");
    				printSpaces(spaceNumber + 7);
    				System.out.print("condition - ");
    				printExpressionTree(theWhile.condition);
    				
    				System.out.println();
    				printSpaces(spaceNumber + 7);
    				
    				System.out.println("body:");
    				printOperators(theWhile.body, spaceNumber + 8);
    			}
    			
    			if (theOp instanceof Return) {					//"return" case
    				Return ret = (Return) theOp;
    				System.out.println("Return");
    				printSpaces(spaceNumber + 7);
    				System.out.print("value - ");
    				printExpressionTree(ret.value);
    				
    				System.out.println();
    			}
    			
    			if (theOp instanceof SimpleExpression) {		//"SimpleExpression" case
    				SimpleExpression expr = (SimpleExpression) theOp;
    				
    				System.out.println("SimpleExpression");
    				printSpaces(spaceNumber + 7);
    				System.out.print("expression - ");
    				printExpressionTree(expr.expression);
    				
    				System.out.println();
    			}
    			
    			if (theOp instanceof Variable) {				//"Variable" case
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
    		System.out.println("null");
    		return;
    	}
    		
    }
    
    private static void printExpressionTree(Expression expr) {
    	
    }
    
    private static void printSpaces(int spaceNumber) {
    	for(int i = 0; i < spaceNumber; i++) {
    		System.out.print(" ");
    	}
    }
}
