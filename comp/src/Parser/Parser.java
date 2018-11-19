package Parser;

import java.io.IOException;


import java.util.ArrayList; 

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import front.Token;
import front.Token.Ident;
import front.Token.KeyWord;
import front.Token.Kind;
import front.Token.Operator;
import front.Token.Operators;
import ir.Function;
import ir.Function.Parameter;
import ir.Type;

import LexerPackage.Reader;

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
	
	private static void parserfunc() {				//parser function, read functions
		String name;								
	    Parameter[] parameters;
	    ir.Operator[] body;
	    Type returnType;
	    int paramNumber;
	    int opNumber;
		
		while(iteration < tokenArr.length) {			
			returnType = readFunctionType();
			name = readFunctionName();											
			readOperator(Operators.OPEN_PARENTHESIS);											//if !"(" throw exception
			parameters = readParemeters();
			readOperator(Operators.OPEN_CURLY_BRACE);											//if !"{" throw exception
			body = parserOp(iteration);
		}
	}
		
	private static ir.Operator[] parserOp(int iter) {					//not ready yet
		if(iteration >= tokenArr.length) {														//out of array bounds exception
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);
		}
		
		Token theToken = tokenArr[iteration];
		if(theToken.getKind() == Kind.KEYWORD) {
			KeyWord theKeyWord = (KeyWord) theToken;
			
			switch(theKeyWord.word) {
				case IF:
				case ELSE:
				case WHILE:
				case RETURN:
				default:
					throw new ParserException("Incorrect KeyWord", theToken.position);
			}
		}
		else {
			throw new ParserException("Error token, has to be an KEYWORD", theToken.position);
		}
		
		//readOperator(Operators.CLOSE_CURLY_BRACE);///
	}
	
	private static Parameter[] readParemeters() {
		if(iteration >= tokenArr.length) {
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);
		}
		
		ArrayList<Parameter> parameterList = new ArrayList<>();
		Parameter theParam;
		Bool haveOperators = new Bool(true);			//link

		while(haveOperators.booleanVal) {
			theParam = readParameter(haveOperators);
			parameterList.add(theParam);
			iteration++;
		}
		Parameter[] returnArr = parameterList.toArray(new Parameter[parameterList.size()]);
		return returnArr;
	}

	private static Parameter readParameter(Bool haveOp) {					//read parameter function, parameter - link on boolean object
		if (iteration > tokenArr.length - 3) {									//exception of index out of Token array bounds during reading the parameter
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);		//throw exception
		}
		String parameterName;						//parameter name variable
		Type parameterType;							//parameter type variable
		
		Token theToken = tokenArr[iteration];			//during token
		
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
			iteration++;							//move token Array index
			theToken = tokenArr[iteration];
			
			if (theToken.getKind() == Kind.IDENT) {			//condition on parameter name
				Ident ident = (Ident) theToken;
				parameterName = ident.word;
			}
			else {
				throw new ParserException("Error token, has to be a parameter name", theToken.position);		//throw condition exception
			}
			iteration++;									//move token Array index
			theToken = tokenArr[iteration];
			
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
		if(iteration >= tokenArr.length) {
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);
		}
		Token theToken = tokenArr[iteration];
		
		if(theToken.getKind() == Kind.OPERATOR) {
			Operator theOp = (Operator) theToken;
			if(theOp.operator != op) {
				throw new ParserException("Incorrect operator, has to be - " + op, theToken.position);
			}
		}
		else {
			throw new ParserException("Error token, has to be an operator", theToken.position);
		}
		iteration++;
	}
	
	private static String readFunctionName() {
		if(iteration >= tokenArr.length) {
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);
		}
		Token theToken = tokenArr[iteration];			//during token
		
		if (theToken.getKind() == Kind.IDENT) {
			Ident ident = (Ident) theToken;
			String funcName = ident.word;
			iteration++;
			return funcName;
		}
		else {
			throw new ParserException("Error token, has to be a name", theToken.position);
		}
	}
	
	private static Type readFunctionType() {
		if(iteration >= tokenArr.length) {
			throw new ParserException("Index out of Token array bounds", tokenArr[tokenArr.length - 1].position);
		}
		Token theToken = tokenArr[iteration];

		if (theToken.getKind() == Kind.KEYWORD) {
			KeyWord keyWord = (KeyWord) theToken;
			
			switch(keyWord.word) {
				case FLOAT:
					iteration++;
					return Type.FLOAT;
				case INT:
					iteration++;
					return Type.INT;
				case VOID:
					iteration++;
					return Type.VOID;
				default:
					throw new ParserException("Null/incorrect function return type", theToken.position);
			}
		}
		throw new ParserException("Error token, has to be a type", theToken.position);						//throw an exception
	}
		
	public static Object[] castToArray(ArrayList<Object> list) {
		Object[] returnArray = new Object[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			returnArray[i] = list.get(i);
		}	
		return returnArray;
	}
	
	public static void main(String[] args) throws IOException{
		Parser theParser = new Parser(Reader.getTokens());
		theParser.parserfunc();
	}
	private static Function getFunction(Type returnType, String name,Parameter[] parameters, ir.Operator[] body) {											//create function
		Function retFunction = new Function(returnType, name, parameters, body);
		return retFunction;
	}

}
