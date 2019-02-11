package Parser;
import java.util.ArrayList;  

import front.Token;
import front.Token.Operator;
import front.Token.Operators;

public class InToPost {				//make postfix entry(include throwing an exception if we have incorrect token/operator)
	ArrayList<Token> output;
	Token[] inputTokenArr;
	java.util.Stack<Token> operationStack; //? rename: operationsStack
	
	public InToPost(Token[] input) {
		inputTokenArr = input;
		operationStack = new java.util.Stack<>();
		output = new ArrayList<>();
	}
	
	ArrayList<Token> doTrans(Token[] inputTokenArr) {

		for (Token theToken : inputTokenArr) {
			switch (theToken.getKind()) { // replace to switch
				case OPERATOR:
					Operator theOp = (Operator) theToken;

					switch (theOp.operator) {
						case OPEN_PARENTHESIS:
							operationStack.push(theOp);            //push in to the stack immediately ( '(' )
							break;
						case CLOSE_PARENTHESIS:
							getParen();                //get all operators to ')' from stack
							break;
						default:
							getOper(theOp, theOp.operator.priority(theOp));    //other operations case
					}
					break;
				case IDENT:
				case INT_LITERAL:
				case FLOAT_LITERAL:
				case STR_LITERAL:
					output.add(theToken);
					break;
				default:
					throw new ParserException("Incorrect token, has to be an ident/operator", theToken.position);    //we have incorrect token
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
			Token theToken = operationStack.peek();			//get top operator
			Operator opTop = (Operator) theToken;
			
			if(opTop.operator == Operators.OPEN_PARENTHESIS) {
				break;
			}
			else {
				int opTopPriority = opTop.operator.priority(opTop);
					
				if (opTopPriority < priority) {		//new operator is more priority then top stack operator
					break;
				}
				else {
					operationStack.pop();	//cut top operator
					output.add(opTop);		//priority of new operator is more/same priority then old operator
				}
			}
		}
		operationStack.push(pasteOp);						//push new operator in the stack
	}
}
