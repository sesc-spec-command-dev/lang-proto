package Parser;
import java.util.ArrayList;  

import front.Token;
import front.Token.Operator;
import front.Token.Operators;
import ir.Expression;
import ir.Expression.Operation;
import ir.Expression.Operand;

public class InToPost {				//make postfix entry(include throwing an exception if we have incorrect token/operator)
	ArrayList<Expression> output;
	Expression[] inputExprArr;
	java.util.Stack<Operation> operationStack; //? rename: operationsStack
	
	public InToPost(Expression[] input) {
		inputExprArr = input;
		operationStack = new java.util.Stack<>();
		output = new ArrayList<>();
	}
	
	ArrayList<Expression> doTrans(Expression[] inputExprArr) {

		for (Expression expr : inputExprArr) {

			if(expr instanceof Operation) {
				Operation operation = (Operation) expr;

				switch(operation.operation.operator) {
					case OPEN_PARENTHESIS:
						operationStack.push(operation);            //push in to the stack immediately ( '(' )
						break;
					case CLOSE_PARENTHESIS:
						getParen();                //get all operators to ')' from stack
						break;
					default:
						getOper(theOp, theOp.operator.priority(theOp));    //other operations case
				}
			}
			else if(expr instanceof Operand){
				Operand operand = (Operand) expr;

				switch(operand.value.getKind()) {
					case IDENT:
					case INT_LITERAL:
					case FLOAT_LITERAL:
					case STR_LITERAL:
						output.add(operand);
						break;
					default:
						throw new ParserException("Incorrect token, has to be an ident/operator", operand.value.position);    //we have incorrect token
				}
			}
		}
			
		while(!operationStack.isEmpty()) {
			output.add(operationStack.pop());			//write to the output string all stack
		}
			
		return output;
	}
		
	private void getParen() {			//was read ')'
		while(!operationStack.isEmpty()) {				//while stack is not empty extract operators
			Token theToken = operationStack.pop();
			Operator theOp = (Operator) theToken;
			
			if(theOp.operator == Operators.OPEN_PARENTHESIS) {						//if new extract operator is '('
				break;								//stop extract operator
			}
			else {
				output.add(theOp);					//push extract operator in output string
			}
		} //? check (
	}
		
	private void getOper(Operator pasteOp, int priority) {
			
		while (!operationStack.isEmpty()) {

			//? use stack.peek()
			Operation operation = operationStack.peek();			//get top operator
			
			if(operation.operation.operator == Operators.OPEN_PARENTHESIS) {
				break;
			}
			else {
				int opTopPriority = operation.operation.operator.priority(operation.operation);
					
				if (opTopPriority < priority) {		//new operator is more priority then top stack operator
					break;
				}
				else {
					operationStack.pop();	//cut top operator
					output.add(operation);		//priority of new operator is more/same priority then old operator
				}
			}
		}
		operationStack.push(pasteOp);						//push new operator in the stack
	}
}
