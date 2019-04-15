package Parser;
import java.util.ArrayList;  

import front.Token.Operators;
import ir.Expression;
import ir.Expression.Operation;
import ir.Expression.Operand;
import ir.Expression.FunctionCall;

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
						getParen(expr);                //get all operators to ')' from stack
						break;
					default:
						getOper(operation, operation.operation.operator.priority(operation.operation));    //other operations case
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
						throw new ParserException("Incorrect operand token, has to be an ident/operator", operand.value.position);    //we have incorrect token
				}
			}
			else {												//function call case
				FunctionCall fCall = (FunctionCall) expr;
				output.add(fCall);								//add function call in output list as simple operand
			}
		}
			
		while(!operationStack.isEmpty()) {
			output.add(operationStack.pop());			//write to the output string all stack
		}
			
		return output;
	}
		
	private void getParen(Expression expr) {			//was read ')'
		boolean b = false;

		while(!operationStack.isEmpty()) {				//while stack is not empty extract operators
			Operation operation = operationStack.pop();
			
			if(operation.operation.operator == Operators.OPEN_PARENTHESIS) {						//if new extract operator is '('
				b = true;
				break;								//stop extract operator
			}
			else {
				output.add(operation);					//push extract operator in output string
			}
		}

		if (!b) {
			throw new ParserException("missed '(' in expression", expr.position());
		}
	}
		
	private void getOper(Operation pasteOperation, int priority) {
			
		while (!operationStack.isEmpty()) {

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
		operationStack.push(pasteOperation);						//push new operator in the stack
	}
}
