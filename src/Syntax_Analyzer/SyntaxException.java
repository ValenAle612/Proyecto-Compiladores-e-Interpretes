package Syntax_Analyzer;

import Lexical_Analyzer.Token;

/**
 *
 * Exception to handle syntax errors.
 * A syntax error occurs when the sequence of tokens violates the grammar rules
 * during the parsing process.
 *
 * This exception provides detailed error information, including:
 *  - The line where the error occurred.
 *  - The expected token versus the actual token encountered.
 *  - A message indicating the specific grammatical expectation.
 *
 */
public class SyntaxException extends Exception{

    public SyntaxException(String expectedToken, Token currentToken){
        super(completeErrorMessage(expectedToken, currentToken));
    }

    public static String completeErrorMessage(String expectedToken, Token currentToken){
        return "Syntax Error at line "+currentToken.getLineNumber()+": "+
                expectedToken+" was expected "+
                "but "+currentToken.getLexeme()+" found \n"+
                "\n [Error:"+currentToken.getLexeme()+"|"+
                    currentToken.getLineNumber()+"]";
    }

}
