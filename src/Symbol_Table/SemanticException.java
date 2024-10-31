package Symbol_Table;

import Lexical_Analyzer.Token;

/**
 * This exception is thrown when a semantic error is detected
 * in the source code, such as type mismatches, undeclared variables,
 * or invalid operations based on the language's rules.
 *
 *  This exception provides detailed error information, including:
 *   - The line where the error occurred.
 *   - The lexeme (or symbol) involved in the error.
 *   - A custom message describing the nature of the semantic issue
 *
 * */
public class SemanticException extends Exception{

    public SemanticException(Token currentToken, String message){
        super( completeErrorMessage(currentToken, message) );
    }

    public static String completeErrorMessage(Token currentToken, String message ){
        return "Semantic error at line "+ currentToken.getLineNumber() +": "+
                message+"\n"+
                "\n [Error:"+ currentToken.getLexeme() +"|"+
                    currentToken.getLineNumber()+"]";
     }

}
