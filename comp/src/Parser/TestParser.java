package Parser;

import java.io.IOException;

import LexerPackage.Reader;

public class TestParser {

	public static void main(String[] args) throws IOException{
		Parser theParser = new Parser(Reader.getTokens());
		theParser.parserfunc();
	}

}
