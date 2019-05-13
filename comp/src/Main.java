import Parser.Parser;
import ir.Function;
import ir.Generator;
import ir.IR;
import ir.Optimizer;
import lexer.Reader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: one arg - file name");
            return;
        }

        Parser theParser = new Parser(Reader.getTokens(args[0]));
        theParser.parserfunc();
        Function[] funcArr = theParser.FunctionOtputList.toArray(new Function[theParser.FunctionOtputList.size()]);
        IR ir = new IR(funcArr);
        Optimizer optimizer = new Optimizer(ir);
        optimizer.optimize();
        ir.printIR("after optimization");
        Generator.generateCode(ir, args[1]);
    }
}
