import Parser.Parser;
import ir.Function;
import ir.Generator;
import ir.IR;
import lexer.Reader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser theParser = new Parser(Reader.getTokens());
        theParser.parserfunc();
        Function[] funcArr = theParser.FunctionOtputList.toArray(new Function[theParser.FunctionOtputList.size()]);
        IR ir = new IR(funcArr);
        ir.printIR("test parsing output");
        Generator.generateCode(ir, "a.txt");
    }
}
