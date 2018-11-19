package Parser;

import java.util.ArrayList;
import front.Token;
import front.Token.Kind;
import front.Token.Operator;
import front.Token.Operators;
import ir.Expression;
import ir.Expression.Operand;
import ir.Expression.Operation;

public class AbstractSintaxTree {
	
	public static class Stack {
		Expression[] stackArray;
		int topInd;
		int maxSize;
		
		public Stack(int maxS) {
			maxSize = maxS;
			stackArray = new Expression[maxS];
			topInd = -1;
		}
		public void push(Expression pushE) {
			topInd++;
			stackArray[topInd] = pushE;
		}
		public Expression pop() {
			Expression returnVal = stackArray[topInd];
			topInd--;
			return returnVal;
		}
		public Expression peek(int ind) {
			return stackArray[ind]; 
		}
		public int size() {
			return topInd + 1;
		}
		public boolean isEmpty() {
			boolean b = false;
			if(topInd == -1) {
				b = true;
			}
			return b;
		}
	}
	
	
	public static Operand buildAST(Token[] inputTokenArr) {				//return head of AST
		InToPost converter = new InToPost(inputTokenArr);
		ArrayList<Token> postfixTokenList = converter.doTrans(inputTokenArr);
		Stack theStack = listToStack(postfixTokenList);
		int ind = theStack.topInd;
		Expression expr;
		
		while(theStack .size() > 1 && ind > 0) {
			expr = theStack.peek(ind);
			
			if (expr.getClass().getName() == "Operation") {			
				Operation theOper = (Operation) expr;
				
				if (theOper.left == null && theOper.right == null) {
					Expression lArg = theStack.pop();
					Expression rArg = theStack.pop();
					
					Operation newOper = new Operation(theOper.operation, lArg, rArg);
					theStack.push(newOper);
				}	
			}
			ind--;
		}
		
		if(theStack.size() > 1) {
			throw new ParserException("Invalid expression", inputTokenArr[theStack.topInd].position);
		}
		
		return (Operand) theStack.pop();
	}
	
	private static Stack listToStack(ArrayList<Token> tokenList) {	
		Stack retStack = new Stack(tokenList.size());
		Token theToken;
		
		for (int i = tokenList.size() - 1; i >= 0; i++) {
			theToken = tokenList.get(i);
			
			if (theToken.getKind() == Kind.OPERATOR) {						//the element is operation
				Operator theOp = (Operator) theToken;
				Operation theOper = new Operation(theOp,null,null);
				retStack.push(theOper);										//push element in stack
			}
			else {
				Operand operand = new Operand(theToken);					//the element is variable/number
				retStack.push(operand);										//push element in stack
			}
		}
		return retStack;
	}
}
