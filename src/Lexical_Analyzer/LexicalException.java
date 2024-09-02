package Lexical_Analyzer;

/**
 *
 * Exception to handle lexical errors.
 * A lexical error occurs when an unrecognized or invalid token
 * is encountered during scanning process.
 *
 * This exception provides detailed error information, including:
 *  - The line and column where the error occurred.
 *  - The full source line containing the error.
 *  - A pointer indicating the exact position of the problematic token.
 *  - The offending lexeme.
 *
 */
public class LexicalException extends Exception{

    public LexicalException(String lexeme, int lineNumber, int columnNumber, String message, String line){
        super(completeErrorMessage(lexeme, lineNumber, columnNumber, message, line));
    }

    private static String completeErrorMessage(String lexeme, int lineNumber, int columnNumber, String message, String line) {
        String detail = "Detail: ";
        String completeMessage = detail + line;
        String pointer = "";

        //moves the pointer to the exact position of the error
        for (int i = 0; i < detail.length() + (columnNumber - 1); i++) {
            if (completeMessage.charAt(i) == '\t')
                pointer += '\t';
            else pointer += ' ';
        }

        pointer += '^';

        return "Lexical error at line " + lineNumber + ": " + message +"\n"+
                "Detail: " + detail + "\n" +
                pointer + "\n" +
                "[Error:" + lexeme + "|" + lineNumber + "] \n";
    }

}
