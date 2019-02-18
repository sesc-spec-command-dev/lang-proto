package Parser;

import java.io.IOException;

import LexerPackage.Reader;
import ir.Function;
import ir.IR;

public class TestParser {

	public static void main(String[] args) throws IOException{
		Parser theParser = new Parser(Reader.getTokens());
		theParser.parserfunc();
		Function[] funcArr = theParser.FunctionOtputList.toArray(new Function[theParser.FunctionOtputList.size()]);
		IR ir = new IR(funcArr);
		ir.printIR("first parsing");
	}

}
