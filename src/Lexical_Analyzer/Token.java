package Lexical_Analyzer;

public class Token {

    private String lexeme;
    private TokenId tokenType;

    private int lineNumber;

    //(TokenId, Lexeme, Line Number)
    public Token(TokenId tokenType, String lexeme, int lineNumber){
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public TokenId getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenId tokenType) {
        this.tokenType = tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
