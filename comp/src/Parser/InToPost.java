package Parser;
import java.util.ArrayList; 

import front.Token;
import front.Token.Kind;
import front.Token.Operator;
import front.Token.Operators;

public class InToPost {				//make postfix entry(include throwing an exception if we have incorrect token/operator)
	private static Stack theStack;
	private static ArrayList<Token> output;
	private static Token[] inputTokenArr;
	
	public InToPost(Token[] input) {
		inputTokenArr = input;
		theStack = new Stack(inputTokenArr.length);
		output = new ArrayList<>();
	}
	
	public static class Stack {
		Token[] stackArray;
		int topInd;
		int maxSize;
		
		public Stack(int maxS) {
			maxSize = maxS;
			stackArray = new Token[maxS];
			topInd = -1;
		}
		public void push(Token pushE) {
			topInd++;
			stackArray[topInd] = pushE;
		}
		public Token pop() {
			Token returnVal = stackArray[topInd];
			topInd--;
			return returnVal;
		}
		public Token peek(int ind) {
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
	
	static ArrayList<Token> doTrans(Token[] inputTokenArr) {
			 
		for (int i = 0; i < inputTokenArr.length; i++) {
			Token theToken = inputTokenArr[i];
			
			if(theToken.getKind() == Kind.OPERATOR) {
				Operator theOp = (Operator) theToken;
				
				switch(theOp.operator) {
					case ADD:
					case SUB:
						getOper(theOp, 1);				//getting of operators, priority - 1 ('+', '-')
						break;
					case MUL:
					case DIV:
						getOper(theOp, 2);				//getting of operators, priority - 2 ('*', '/')
						break;
					case OPEN_PARENTHESIS:
						theStack.push(theOp);			//push in to the stack immediately ( '(' )
						break;
					case CLOSE_PARENTHESIS:
						getParen();				//get all operators to ')' from stack
						break;
					default:
						throw new ParserException("Incorrect operator", theOp.position);
				}
			}
			else {
				if(theToken.getKind() == Kind.IDENT) {
					output.add(theToken);
				}
				else {
					throw new ParserException("Incorrect token, has to be an ident/operator", theToken.position);	//we have incorrect token
				}
			}
		}
			
		while(!theStack.isEmpty()) {
			output.add(theStack.pop());			//write to the output string all stack
		}
			
		return output;
	}
		
	private static void getParen() {			//was read ')'
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
		
	private static void getOper(Operator pasteOp, int priority) {
			
		while(!theStack.isEmpty()) {
			Token theToken = theStack.pop();			//cut top operator
			Operator opTop = (Operator) theToken;
			
			if(opTop.operator == Operators.OPEN_PARENTHESIS) {
				theStack.push(opTop);				//push '(' in the stack 
				break;
			}
			else {
				int opTopPriority = getPriority(opTop);
					
				if(opTopPriority < priority) {		//new operator is more priority then top stack operator
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
	
	static int getPriority(Operator op) {
		int priority = 0;
		
		switch(op.operator) {
			case ADD:
			case SUB:
				priority = 1;
				break;
			case MUL:
			case DIV:
				priority = 2;
				break;
			default:
				////add more operators cases later
			break;
		}
		return priority;
	}	
}
