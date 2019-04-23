package Parser;
import front.Position;

public class ParserException extends Error {
    public final Position exceptionPos; //? Position, public final

    public ParserException(String exText, Position pos) {
        super("\n" + exText + "\nRow: " + pos.row + "\nColumn: " + pos.column);

        this.exceptionPos = pos;
    }
}
