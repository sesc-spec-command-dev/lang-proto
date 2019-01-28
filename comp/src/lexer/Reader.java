package LexerPackage;
import front.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    public static char[] readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(new File(fileName));
        BufferedReader reader = new BufferedReader(fileReader);

        StringBuilder sbFileContent = new StringBuilder();

        String curLine;
        while ((curLine = reader.readLine()) != null) {
            sbFileContent.append(curLine+"\n");
        }
        sbFileContent.deleteCharAt(sbFileContent.length()-1);

        fileReader.close();
        reader.close();

        char[] fileContent = new char[sbFileContent.length()];
        for (int i = 0; i < sbFileContent.length(); i++) {
            fileContent[i] = sbFileContent.charAt(i);
        }

        return fileContent;
    }

    public static void main(String[] args) {
        for (Token x : getTokens()) System.out.println(x);
    }

    public static Token[] getTokens() throws IOException {
        Token[] tokens = Lexer.makeTokens(readFile("input.txt"));

/*      for (Token token : tokens) {
            System.out.println(token.getKind() + " " + token.position.row + " " + token.position.column);
        }
*/		
        return tokens;
    }
}
