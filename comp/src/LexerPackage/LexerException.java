package LexerPackage;
import front.Position;

public class LexerException extends Error {
    public final Position position; //? Position, public final

    public LexerException(String message, Position position) {
        super("\n" + message + "\nRow is " + position.row + "\nColumn is " + position.column);

        this.position = position;
    }
}
