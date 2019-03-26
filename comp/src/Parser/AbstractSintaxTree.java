package Parser;

import java.util.ArrayList; 
import front.Token;
import front.Token.Kind;
import front.Token.Operator;
import ir.Expression;
import ir.Expression.Operand;
import ir.Expression.Operation;

public class AbstractSintaxTree {
	
	static Expression buildAST(Token[] inputTokenArr, boolean isReturnExpr) {	
		
		InToPost converter = new InToPost(inputTokenArr);
		ArrayList<Token> tokenList = converter.doTrans(inputTokenArr);

		java.util.Stack<Expression> theStack = new java.util.Stack<>();
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
		
		if(theStack.size() > 1 || (!(theStack.peek() instanceof Operation) && isReturnExpr != true)) {
			throw new ParserException("Invalid expression", theStack.peek().position());			//the tree do not build correctly
		}
		return theStack.pop();
	}
}
