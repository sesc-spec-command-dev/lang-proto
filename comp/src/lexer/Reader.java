package lexer;
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

    public static void main(String[] args) throws IOException {
        for (Token x : getTokens("input.txt")) System.out.println(x.getKind() + " " + x.position.row + " " + x.position.column);
    }

    public static Token[] getTokens(String fileName) throws IOException {
        Token[] tokens = Lexer.makeTokens(readFile(fileName));

/*      for (Token token : tokens) {
            System.out.println(token.getKind() + " " + token.position.row + " " + token.position.column);
        }
*/		
        return tokens;
    }
}
