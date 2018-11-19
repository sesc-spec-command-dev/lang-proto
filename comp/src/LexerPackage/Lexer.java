package LexerPackage;
import front.Position;
import front.Token;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static Token[] makeTokens(char[] text) {
        int length = text.length;

        ArrayList<Token> tokens = new ArrayList<>();

        int row = 1;
        int column = 0;
        for (int i = 0; i < length; i++) {
            column++;
            if (!Character.isWhitespace(text[i])) {
                String sToken = getWord(text, i);
                i += sToken.length()-1;

                MyPair<Token.Kind, Object> pair = identify(sToken);
                Token.Kind kind = pair.getElement0();
                Object value = pair.getElement1();

                if (kind == null) {
                    throw new LexerException("Can't identify token", new Position(row, column));
                }

                tokens.add(Token.make(kind, new Position(row, column), value));
            } else if (text[i] == '\n') {
                column = 0;
                row++;
            }
        }

        Token[] result = new Token[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            result[i] = tokens.get(i);
        }

        return result;
    }

    private static String getWord(char[] text, int start) {
        String word = "";

        for (int i = start; i < text.length; i++) {
            if (i != text.length-1) {
                if (isOperator(String.valueOf(text[i]) + text[i + 1]) != null && !word.equals("")) {
                    return word;
                } else if (word.equals("") && isOperator(String.valueOf(text[i]) + text[i + 1]) != null) {
                    return String.valueOf(text[i] + text[i + 1]);
                }
            }

            if (isOperator(String.valueOf(text[i])) != null && !word.equals("")) return word;

            if (isOperator(String.valueOf(text[i])) != null && word.equals("")) return String.valueOf(text[i]);

            if (!Character.isWhitespace(text[i])) {
                word += (text[i]);
            } else {
                return word;
            }
        }

        return word;
    }

    private static MyPair<Token.Kind, Object> identify(String sToken) {
        Token.Kind kind = null;
        Object value = null;

        Pattern p1 = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*");
        Matcher m1 = p1.matcher(sToken);

        if (m1.matches()) {
            Object kValue = isKeyword(sToken);
            if (kValue != null) {
                kind = Token.Kind.KEYWORD;
                value = kValue;
            } else {
                kind = Token.Kind.IDENT;
                value = sToken;
            }
        } else {
            Token.Kind litKind = isLiteral(sToken);

            if (litKind == null) {
                Object oValue = isOperator(sToken);
                if (oValue != null) {
                    kind = Token.Kind.OPERATOR;
                    value = oValue;
                }
            } else {
                kind = litKind;
                switch (kind) {
                    case INT_LITERAL:
                        value = Integer.parseInt(sToken);
                        break;
                    case FLOAT_LITERAL:
                        value = Float.parseFloat(sToken);
                        break;
                    case STR_LITERAL:
                        value = sToken;
                }
            }
        }

        return new MyPair<>(kind, value);
    }

    private static Object isKeyword(String sToken) {
        Token.KeyWords[] words = Token.KeyWords.values();
        for (Token.KeyWords word: words) {
            if (word.getText().equals(sToken)) {
                return word;
            }
        }
        return null;
    }

    private static Token.Kind isLiteral(String sToken) {
        Token.Kind kind = null;

        Pattern intP = Pattern.compile("[0-9]+");
        Pattern floatP = Pattern.compile("[0-9]+[.][0-9]+");
        Pattern stringP = Pattern.compile("[\"][^\"]*[\"]");

        if (intP.matcher(sToken).matches()) {
            kind = Token.Kind.INT_LITERAL;
        } else if (floatP.matcher(sToken).matches()) {
            kind = Token.Kind.FLOAT_LITERAL;
        } else if (stringP.matcher(sToken).matches()) {
            kind = Token.Kind.STR_LITERAL;
        }

        return kind;
    }

    private static Object isOperator(String sToken) {
        Token.Operators[] operators = Token.Operators.values();
        for (Token.Operators operator: operators) {
            if (operator.value.equals(sToken)) {
                return operator;
            }
        }
        return null;
    }
}
