package Parser;
import java.util.ArrayList;  

import front.Token;
import front.Token.Kind;
import front.Token.Operator;
import front.Token.Operators;

public class InToPost {				//make postfix entry(include throwing an exception if we have incorrect token/operator)
	ArrayList<Token> output;
	Token[] inputTokenArr;
	java.util.Stack<Token> theStack;
	
	public InToPost(Token[] input) {
		inputTokenArr = input;
		theStack = new java.util.Stack<>();
		output = new ArrayList<>();
	}
	
	ArrayList<Token> doTrans(Token[] inputTokenArr) { 
		
		for (int i = 0; i < inputTokenArr.length; i++) {
			Token theToken = inputTokenArr[i];
			
			switch(theToken.getKind()) { // replace to switch
				case OPERATOR:
					Operator theOp = (Operator) theToken;
				
					switch(theOp.operator) {
						case OPEN_PARENTHESIS:
							theStack.push(theOp);			//push in to the stack immediately ( '(' )
							break;
						case CLOSE_PARENTHESIS:
							getParen();				//get all operators to ')' from stack
							break;
						default:
							getOper(theOp, theOp.operator.priority(theOp));	//other operations case
					}
					break;
				case IDENT:
				case INT_LITERAL:
				case FLOAT_LITERAL:
				case STR_LITERAL:
					output.add(theToken);
					break;
				default:
					throw new ParserException("Incorrect token, has to be an ident/operator", theToken.position);	//we have incorrect token
			}
		}
			
		while(!theStack.isEmpty()) {
			output.add(theStack.pop());			//write to the output string all stack
		}
			
		return output;
	}
		
	private void getParen() {			//was read ')'
		while(!theStack.isEmpty()) {				//while stack is not empty extract operators
			Token theToken = theStack.pop();
			Operator theOp = (Operator) theToken;
			
			if(theOp.operator == Operators.OPEN_PARENTHESIS) {						//if new extract operator is '('
				break;								//stop extract operator
			}
			else {
				output.add(theOp);					//push extract operator in output string
			}
		}
	}
		
	private void getOper(Operator pasteOp, int priority) {
			
		while (!theStack.isEmpty()) {
			Token theToken = theStack.pop();			//cut top operator
			Operator opTop = (Operator) theToken;
			
			if(opTop.operator == Operators.OPEN_PARENTHESIS) {
				theStack.push(opTop);				//push '(' in the stack 
				break;
			}
			else {
				int opTopPriority = opTop.operator.priority(opTop);
					
				if (opTopPriority < priority) {		//new operator is more priority then top stack operator
					theStack.push(opTop);			//save old operator
					break;
				}
				else {
					output.add(opTop);		//priority of new operator is more/same priority then old operator
				}
			}
		}
		theStack.push(pasteOp);						//push new operator in the stack
	}
}
