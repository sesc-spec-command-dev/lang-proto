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
	
	static Expression buildAST(Token[] inputTokenArr, boolean isReturnExpr) {	
		
		InToPost converter = new InToPost(inputTokenArr);
		ArrayList<Token> tokenList = converter.doTrans(inputTokenArr);

		Stack theStack = new Stack(tokenList.size());
		Token theToken;
		
		for (int i = 0; i < tokenList.size(); i++) {
			theToken = tokenList.get(i);
			
			if (theToken.getKind() == Kind.OPERATOR) {						//the element is operation
				Operator theOp = (Operator) theToken;
				Expression rightExpr = theStack.pop();
				Expression leftExpr = theStack.pop();
				
				Operation theOper = new Operation(theOp,leftExpr,rightExpr);
				theStack.push(theOper);
			}
			else {
				Operand operand = new Operand(theToken);					//the element is variable/number
				theStack.push(operand);										//push element in stack
			}
		}
		
		if(theStack.size() > 1 || (!(theStack.peek(theStack.topInd) instanceof Operation) && isReturnExpr != true)) {
			throw new ParserException("Invalid expression", inputTokenArr[theStack.topInd].position);			//the tree do not build correctly
		}
		return theStack.pop();
	}
}
