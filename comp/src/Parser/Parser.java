package Parser;

import java.util.ArrayList;
import front.Token;
import front.Token.Ident;
import front.Token.KeyWord;
import front.Token.Kind;
import front.Token.Operator;
import front.Token.Operators;
import ir.Expression;
import ir.Function;
import ir.Function.Parameter;
import ir.Operator.If;
import ir.Operator.Return;
import ir.Operator.SimpleExpression;
import ir.Operator.Variable;
import ir.Operator.While;
import ir.Type;
import front.Position;
import ir.Expression.FunctionCall;
import ir.Expression.Operation;
import ir.Expression.Operand;
import ir.Operator.Write;

public class Parser {
	static Token[] tokenArr;							//Token Array
	static ArrayList<Function> FunctionOtputList;		//output list of functions
	static boolean haveExeption;						//boolean variable of exception exist
	static int iteration;								//now iteration in Token array
	
	public Parser(Token[] inputTokenArr) {			//Parser constructor
		tokenArr = inputTokenArr;
		FunctionOtputList = new ArrayList<>();
		haveExeption = false;
	}
	
	public static void parserfunc() {				//parser function, read functions
		String name;								
	    Parameter[] parameters;
	    ir.Operator[] body;
	    ArrayList<ir.Operator> bodyList = new ArrayList<>();
	    Type returnType;
	    
		while(iteration < tokenArr.length) {			
			returnType = readFunctionType();
			name = readFunctionName();											
			readOperator(Operators.OPEN_PARENTHESIS);											//if !"(" throw exception
			parameters = readParemeters();
			readOperator(Operators.OPEN_CURLY_BRACE);											//if !"{" throw exception
			bodyList = parserOp(bodyList);
			body = bodyList.toArray(new ir.Operator[bodyList.size()]);
			FunctionOtputList.add(new Function(returnType, name, parameters, body));			//add new function in the function list
			bodyList.clear();																	//clear body list
		}
	}
		
	private static ArrayList<ir.Operator> parserOp(ArrayList<ir.Operator> operatorList) {		//recursive function of reading parameters, returns parameter list					
		Token theToken = getIterToken();
			
		if(theToken.getKind() == Kind.KEYWORD) {			//if next token is an operator
			KeyWord theKeyWord = (KeyWord) theToken;
			Expression condition;							//operator condition
			ir.Operator[] body;								//operator body
			ArrayList<ir.Operator> bodyList = new ArrayList<>();			//operator body list
			
			switch(theKeyWord.word) {
				case IF:
					readOperator(Operators.OPEN_PARENTHESIS);		//read '('
					condition = getExpression(true, new Bool(false), false);
					readOperator(Operators.OPEN_CURLY_BRACE);		//read '{'
  
					bodyList = parserOp(bodyList);
					body = bodyList.toArray(new ir.Operator[bodyList.size()]);	
					If theIf = new If(condition,body, null);
					operatorList.add(theIf);						//add new parameter in the function parameter list
					break;
					
				case ELSE:
					readOperator(Operators.OPEN_CURLY_BRACE);
					ir.Operator theOp = operatorList.get(operatorList.size() - 1);
					
					if(!(theOp instanceof If)) {						//condition on "if" part in the list end
						throw new ParserException("must not be an operator between if & else", theToken.position);
					}
					If lastIf = (If) theOp;
					bodyList = parserOp(bodyList);
					body = bodyList.toArray(new ir.Operator[bodyList.size()]);	
					
					If newIf = new If(lastIf.condition, lastIf.thenPart, body);					//add "then" part to operator
					operatorList.remove(operatorList.size() - 1);								//cut top "If"
					operatorList.add(newIf);								//add new parameter in the function parameter list
					break;
					
				case WHILE:
					readOperator(Operators.OPEN_PARENTHESIS);		//read '('
					condition = getExpression(true, new Bool(false), false);
					readOperator(Operators.OPEN_CURLY_BRACE);		//read '{'
					
					bodyList = parserOp(bodyList);
					body = bodyList.toArray(new ir.Operator[bodyList.size()]);	
					
					While theWhile = new While(condition, body);
					operatorList.add(theWhile);							//add new parameter in the function parameter list
					break;
					
				case RETURN:
					boolean marker = true;		//in RETURN we may have expression consists of one word/number
					condition = getExpression(false,new Bool(false), marker);
					Return ret = new Return(condition);
					operatorList.add(ret);								//add new parameter in the function parameter list
					break;

				case WRITE:
					readOperator(Operators.OPEN_PARENTHESIS);			//read '('
					Expression writeExp = getExpression(true, new Bool(false), true);	//get print expression (we may have expression consists of one word/number)
					Write write = new Write(writeExp);
					operatorList.add(write);
					readOperator(Operators.SEMICOLON);					//';' at the end of Write operator
					break;

				case INT:												//variable declaration
					operatorList.add(getVariable(Type.INT));
					break;
					
				case FLOAT:												//variable declaration
					operatorList.add(getVariable(Type.FLOAT));          
					break;
					
				default:
					throw new ParserException("Incorrect KeyWord", theToken.position);
			}
		}
		else {
			if (theToken.getKind() == Kind.IDENT || theToken.getKind() == Kind.INT_LITERAL || theToken.getKind() == Kind.FLOAT_LITERAL || theToken.getKind() == Kind.STR_LITERAL) {			//if the next Token is expression
				Bool falseVal = new Bool(false);
				iteration--;
				Expression expr = getExpression(false, falseVal, false);
				SimpleExpression theExpr = new SimpleExpression(expr);
				operatorList.add(theExpr);								//add new parameter in the function parameter list
			}
			else {
				if (theToken.getKind() == Kind.OPERATOR) {
      				iteration--;
					readOperator(Operators.CLOSE_CURLY_BRACE);			//if during token is '}'
					return operatorList;								//return operator list
				}

				throw new ParserException("Invalide function body", theToken.position);
			}
		}
		return parserOp(operatorList);		
	}

	private static Variable getVariable(Type theType) {
		Token theToken = getIterToken();
		String name;
		
		if(theToken.getKind() == Kind.IDENT) {
			Ident strIdent = (Ident) theToken;
			
			name = strIdent.word;
			theToken = getIterToken();
			
			if(theToken.getKind() == Kind.OPERATOR) {
				Operator theOp = (Operator) theToken;
				
				if(theOp.operator != Operators.SEMICOLON) {
					throw new ParserException("Incorrect operator, should be ';'", theToken.position);
				}
			}
			else {
				throw new ParserException("Complete ';' after variable declaration", theToken.position);
			}
		}
		else {
			throw new ParserException("Incorrect variable name", theToken.position);
		}	
		return new Variable(theType, name);
	}
	
	private static Expression getExpression(boolean inCondition, Bool isCall, boolean marker) {
		ArrayList<Expression> expressionList = new ArrayList<>();
		boolean breakFactor;
		Expression oper;
		boolean oneArgPossib = marker;									//one argument possibility
		
		int parenthesisNumber = 0;
		Position exceptionPos = null;

		while (true) {
			breakFactor = false;
			Token theToken = getIterToken();
			exceptionPos = theToken.position;
			
			if (theToken.getKind() == Kind.OPERATOR) {
				Operator theOp = (Operator) theToken;

				switch (theOp.operator) {

					case COMMA:
						if (isCall.booleanVal) {
							oneArgPossib = true;                                //possibility of one argument in expression - true
							breakFactor = true;
						} else {
							throw new ParserException("Error argument - (',') in expression", exceptionPos);
						}
						break;

					case OPEN_PARENTHESIS:
						oper = new Operation(theOp, null, null);
						expressionList.add(oper);
						parenthesisNumber++;                                    //we have new OPEN_PARENTHESIS in expression
						break;

					case CLOSE_PARENTHESIS:
						parenthesisNumber--;
						if (inCondition || isCall.booleanVal) {//if expression situate in condition, number of ')' bigger then number of '(' on 1

							if (parenthesisNumber < 0) {
								breakFactor = true;				//expression reading complete
								if(isCall.booleanVal) {
									isCall.booleanVal = false;
									oneArgPossib = true;
									parenthesisNumber++;
								}
							}
							else {
								oper = new Operation(theOp, null, null);
								expressionList.add(oper);
							}
						}
						break;

					case SEMICOLON:	//if expression situate in operator body
						breakFactor =  true;
						break;

					default:
						oper = new Operation(theOp, null, null);
						expressionList.add(oper);
				}

				if (breakFactor) {
					break;
				}
			}
			else {
				boolean b = false;

				if (theToken.getKind() == Kind.IDENT) {
					Ident name = (Ident) theToken;
					Token next = getIterToken();

					if (next.getKind() == Kind.OPERATOR) {
						Operator op = (Operator) next;

						if (op.operator == Operators.OPEN_PARENTHESIS) {
							ArrayList<Expression> exprList = new ArrayList<>();

							Bool isCallVal = new Bool(true);

							while (isCallVal.booleanVal) {
								exprList.add(getExpression(false, isCallVal, false));
							}

							Expression[] parameterArr = exprList.toArray(new Expression[exprList.size()]);
							FunctionCall fCall = new FunctionCall(name, parameterArr);
							expressionList.add(fCall);
							oneArgPossib = true;
							b = true;
						}
						else {
							iteration--;										//it is not function call, move iteration back
						}
					}
					else {
						iteration--;										//it is not function call, move iteration back
					}
				}
				if (!b) {
					if (theToken.getKind() != Kind.IDENT && theToken.getKind() != Kind.INT_LITERAL && theToken.getKind() != Kind.FLOAT_LITERAL && theToken.getKind() != Kind.STR_LITERAL) {
						throw new ParserException("Error argument in expression", exceptionPos);
					}
					Operand operand = new Operand(theToken);            //if exception was not throwed - add new operand in expression list
					expressionList.add(operand);
				}
			}
		}

		if (parenthesisNumber != 0 && !isCall.booleanVal && !inCondition) {
			throw new ParserException("invalide expression", exceptionPos);
		}

		Expression[] exprArr = expressionList.toArray(new Expression[expressionList.size()]);
		Expression retExpr = AbstractSintaxTree.buildAST(exprArr, oneArgPossib);
		return retExpr;
	}

	private static Parameter[] readParemeters() {
		ArrayList<Parameter> parameterList = new ArrayList<>();
		Parameter theParam;
		Bool haveOperators = new Bool(true);			//link
		
		Token theToken = getIterToken();				//get Token only to show
		
		if (theToken.getKind() == Kind.OPERATOR) {
			Operator theOp = (Operator) theToken;
			
			if (theOp.operator == Operators.CLOSE_PARENTHESIS) {// condition on empty parameter list
				return null;									//empty parameter list
			}
			throw new ParserException("incorrect operator in parameter list body",theToken.position);
		}
		else {
			iteration--;
			while (haveOperators.booleanVal) {
				theParam = readParameter(haveOperators);
				parameterList.add(theParam);
			}
			Parameter[] returnArr = parameterList.toArray(new Parameter[parameterList.size()]);
			return returnArr;
		}
	}

	private static Parameter readParameter(Bool haveOp) {					//read parameter function, parameter - link on boolean object
		String parameterName;						//parameter name variable
		Type parameterType;							//parameter type variable
		
		Token theToken = getIterToken();			//during token
		
		if (theToken.getKind() == Kind.KEYWORD) {			//condition on parameter type
			KeyWord keyWord = (KeyWord) theToken;
			
			switch (keyWord.word) {
				case FLOAT:
					parameterType = Type.FLOAT;
					break;
				case INT:
					parameterType = Type.INT;
					break;
				default:
					throw new ParserException("Error parameter type", theToken.position);
			}
			theToken = getIterToken();
			
			if (theToken.getKind() == Kind.IDENT) {			//condition on parameter name
				Ident ident = (Ident) theToken;
				parameterName = ident.word;
			}
			else {
				throw new ParserException("Error token, has to be a parameter name", theToken.position);		//throw condition exception
			}
			theToken = getIterToken();
			
			if (theToken.getKind() == Kind.OPERATOR) {		//condition on operator after parameter name
				Operator theOp = (Operator) theToken;
				
				switch(theOp.operator) {
					case COMMA:
						return new Parameter(parameterName, parameterType);
					case CLOSE_PARENTHESIS:
						haveOp.booleanVal = false;
						return new Parameter(parameterName, parameterType);
					default:
						throw new ParserException("Incorrect operator, has to be: ')' or ','", theToken.position);		//throw condition exception
				}
			}
			else {
				throw new ParserException("Error Token, has to be an operator ')' or ','", theToken.position);		//throw condition exception
			}
		}
		else {
			throw new ParserException("Error Token, has to be a parameter Type", theToken.position);		//throw condition exception
		}
	}
	
	private static void readOperator(Operators op) {			//throw an exception if incorrect operator/(input token)
		Token theToken = getIterToken();
		
		if(theToken.getKind() == Kind.OPERATOR) {
			Operator theOp = (Operator) theToken;
			if(theOp.operator != op) {
				throw new ParserException("Incorrect operator, has to be - " + op, theToken.position);
			}
		}
		else {
			throw new ParserException("Error token, has to be an operator", theToken.position);
		}
	}
	
	private static String readFunctionName() {
		Token theToken = getIterToken();			//during token
		
		if (theToken.getKind() == Kind.IDENT) {
			Ident ident = (Ident) theToken;
			String funcName = ident.word;
			return funcName;
		}
		else {
			throw new ParserException("Error token, has to be a name", theToken.position);
		}
	}
	
	private static Token getIterToken() {		//returns the iteration token
		if(iteration >= tokenArr.length) {		//throw an exception if now token is out of array bounds
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);	
		}
		Token theToken = tokenArr[iteration];
		iteration++;							//move token Array index
		return theToken;
	}
	
	private static Type readFunctionType() {
		Token theToken = getIterToken();

		if (theToken.getKind() == Kind.KEYWORD) {
			KeyWord keyWord = (KeyWord) theToken;
			
			switch(keyWord.word) {
				case FLOAT:
					return Type.FLOAT;
				case INT:
					return Type.INT;
				case VOID:
					return Type.VOID;
				default:
					throw new ParserException("Null/incorrect function return type", theToken.position);
			}
		}
		throw new ParserException("Error token, has to be a type", theToken.position);						//throw an exception
	}
}
