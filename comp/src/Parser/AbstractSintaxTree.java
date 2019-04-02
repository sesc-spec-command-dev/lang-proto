package Parser;

import java.util.ArrayList;
import ir.Expression;
import ir.Expression.Operand;
import ir.Expression.Operation;

public class AbstractSintaxTree {
	
	static Expression buildAST(Expression[] inputExprArr, boolean oneArgumentPossib) {
		
		InToPost converter = new InToPost(inputExprArr);
		ArrayList<Expression> exprList = converter.doTrans(inputExprArr);

		java.util.Stack<Expression> theStack = new java.util.Stack<>();
		Expression expr;
		
		for (int i = 0; i < exprList.size(); i++) {
			expr = exprList.get(i);
			
			if (expr instanceof Operation) {						//the element is operation
				Operation theOper = (Operation) expr;
				Expression rightExpr = theStack.pop();
				Expression leftExpr = theStack.pop();
				
				theOper.right = rightExpr;
				theOper.left = leftExpr;
				theStack.push(theOper);
			}
			else {
				Operand operand = (Operand) expr;					//the element is variable/number
				theStack.push(operand);										//push element in stack
			}
		}
		
		if(theStack.size() > 1 || (!(theStack.peek() instanceof Operation) && oneArgumentPossib != true)) {
			throw new ParserException("Invalid expression", theStack.peek().position());			//the tree do not build correctly
		}
		return theStack.pop();
	}
}
