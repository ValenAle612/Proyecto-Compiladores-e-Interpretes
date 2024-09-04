package Lexical_Analyzer;

import java.io.IOException;
import Source_Manager.*;

public class LexicalAnalyzer {

    String lexeme;
    char currentChar;
    SourceManager sourceManager;
    MiniJava_ReservedKeywords reservedKeywords;

    String multiline_comment_line;
    int multiline_comment_row, multiline_comment_column;

    public LexicalAnalyzer(SourceManager sourceManager) throws IOException{
        this.sourceManager = sourceManager;
        this.currentChar = sourceManager.getNextChar();

        reservedKeywords = new MiniJava_ReservedKeywords();
    }

    public Token nextToken() throws LexicalException, IOException{
        lexeme = "";
        return initial_state();
    }

    public void updateCurrentChar() throws  IOException{
        this.currentChar = sourceManager.getNextChar();
    }

    private void updateLexeme(){
        this.lexeme = this.lexeme + currentChar;
    }

    public Token initial_state() throws LexicalException, IOException { //e0 state of the automaton

        if( Character.isWhitespace(currentChar) ){
            updateCurrentChar();

            return initial_state();
        } else if ( Character.isUpperCase(currentChar) ){
            updateLexeme();
            updateCurrentChar();

            return upperCase_state();
        } else if ( Character.isLowerCase(currentChar) ){
            updateLexeme();
            updateCurrentChar();

            return lowerCase_state();
        } else if ( Character.isDigit(currentChar) ) {
            updateLexeme();
            updateCurrentChar();

            return digit_state();
        } else if ( currentChar == '"' ){//String
            updateLexeme();
            updateCurrentChar();

            return strings_state();
        } else if ( currentChar == '(' ) { //puntuation symbols
            updateLexeme();
            updateCurrentChar();

            return openParenthesis();
        } else if ( currentChar == ')' ) {
            updateLexeme();
            updateCurrentChar();

            return closeParenthesis();
        } else if ( currentChar == '{' ) {
            updateLexeme();
            updateCurrentChar();

            return openBrace();
        } else if ( currentChar == '}' ) {
            updateLexeme();
            updateCurrentChar();

            return closeBrace();
        } else if ( currentChar == ',' ) {
            updateLexeme();
            updateCurrentChar();

            return comma();
        } else if ( currentChar == '.' ) {
            updateLexeme();
            updateCurrentChar();

            return dot();
        } else if ( currentChar == ';' ) {
            updateLexeme();
            updateCurrentChar();

            return semicolon();
        } else if ( currentChar == ':' ) {
            updateLexeme();
            updateCurrentChar();

            return colon();
        } else if ( currentChar == '\'' ) {
            updateLexeme();
            updateCurrentChar();

            return character_state();
        } else if ( currentChar == '>' ) { //operators
            updateLexeme();
            updateCurrentChar();

            return greaterThan();
        } else if ( currentChar == '<' ) {
            updateLexeme();
            updateCurrentChar();

            return lessThan();
        } else if ( currentChar == '!' ) {
            updateLexeme();
            updateCurrentChar();

            return not_state();
        } else if ( currentChar == '=' ) {
            updateLexeme();
            updateCurrentChar();

            return assingment_state();
        } else if ( currentChar == '+' ){
            updateLexeme();
            updateCurrentChar();

            return add_state();
        }  else if ( currentChar == '-' ){
            updateLexeme();
            updateCurrentChar();

            return substract_state();
        }  else if ( currentChar == '*' ){
            updateLexeme();
            updateCurrentChar();

            return multiply_state();
        }  else if ( currentChar == '/' ){
            updateLexeme();
            updateCurrentChar();

            return divide_state();
        } else if ( currentChar == '&' ){
            updateLexeme();
            updateCurrentChar();
            return and_state();
        } else if ( currentChar == '|' ){
            updateLexeme();
            updateCurrentChar();
            return or_state();
        } else if ( currentChar == '%' ){
            updateLexeme();
            updateCurrentChar();
            return mod_state();
        } else if ( sourceManager.isEndOfFile(currentChar) ) {
            return endOfFile();
        }else{
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "invalid symbol", sourceManager.getPreviousLine());
        }
    }

    private Token upperCase_state() throws LexicalException, IOException{
        if( Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_'){
            updateLexeme();
            updateCurrentChar();
            return upperCase_state();
        }else{
            TokenId tokenId = reservedKeywords.getTokenId(lexeme);
            if( tokenId == null )
                return new Token( TokenId.class_id, lexeme, sourceManager.getLineNumber() );
            else
                return new Token( tokenId, lexeme, sourceManager.getLineNumber() );
        }
    }

    private Token lowerCase_state() throws LexicalException, IOException{
        if( Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_'){
            updateLexeme();
            updateCurrentChar();
            return lowerCase_state();
        }else{
            TokenId tokenId = reservedKeywords.getTokenId(lexeme);
            if( tokenId == null )
                return new Token( TokenId.method_id, lexeme, sourceManager.getLineNumber() );
            else
                return new Token( tokenId, lexeme, sourceManager.getLineNumber() );
        }
    }

    private Token character_state() throws LexicalException, IOException{
        if( currentChar == '\\' ){
            updateLexeme();
            updateCurrentChar();

            return char_with_Backslash();
        }else if ( currentChar == '\n' || currentChar == '\'' ) {
            updateLexeme();

            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(),
                    "invalid character", sourceManager.getPreviousLine());
        }else if ( sourceManager.isEndOfFile(currentChar) ){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(),
                    "invalid character", sourceManager.getPreviousLine());
        } else {
            updateLexeme();
            updateCurrentChar();

            return character_state();
        }
    }

    private Token character_state2() throws LexicalException, IOException{
        if( currentChar == '\'' || currentChar == '\t' ){
            updateLexeme();
            updateCurrentChar();

            return new Token( TokenId.literal_char, lexeme, sourceManager.getLineNumber() );
        }else
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "invalid character -> poorly closed character", sourceManager.getPreviousLine());
    }

    private Token char_with_Backslash() throws LexicalException, IOException{
        if( currentChar == '\n' || sourceManager.isEndOfFile(currentChar) ){
            updateLexeme();
            updateCurrentChar();

            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "invalid character", sourceManager.getPreviousLine());
        } else {
            updateLexeme();
            updateCurrentChar();

            return character_state2();
        }
    }

    private Token digit_state() throws LexicalException, IOException {
        if( Character.isDigit(currentChar) && lexeme.length() <= 9){
            updateLexeme();
            updateCurrentChar();
            return digit_state();
        }else{
            if( lexeme.length() <= 9 )
                return new Token( TokenId.literal_integer, lexeme, sourceManager.getLineNumber() );
            else
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber()-1,
                        " Number out of bounds -> exceeds 9 digits ", sourceManager.getPreviousLine());
        }
    }

    private Token openParenthesis() throws LexicalException, IOException{
        return new Token( TokenId.ps_openParenthesis, lexeme, sourceManager.getLineNumber() );
    }

    private Token closeParenthesis() throws LexicalException, IOException{
        return new Token( TokenId.ps_closeParenthesis, lexeme, sourceManager.getLineNumber() );
    }

    private Token openBrace() throws LexicalException, IOException{
        return new Token( TokenId.ps_openBrace, lexeme, sourceManager.getLineNumber() );
    }

    private Token closeBrace() throws LexicalException, IOException{
        return new Token( TokenId.ps_closeBrace, lexeme, sourceManager.getLineNumber() );
    }

    private Token comma() throws LexicalException, IOException{
        return new Token( TokenId.ps_comma, lexeme, sourceManager.getLineNumber() );
    }

    private Token dot() throws LexicalException, IOException{
        return new Token( TokenId.ps_dot, lexeme, sourceManager.getLineNumber() );
    }

    private Token semicolon() throws LexicalException, IOException{
        return new Token( TokenId.ps_semicolon, lexeme, sourceManager.getLineNumber() );
    }

    private Token colon() throws LexicalException, IOException{
        return new Token( TokenId.ps_colon, lexeme, sourceManager.getLineNumber() );
    }

    private Token greaterThan() throws IOException {
        if( currentChar == '='){
            updateLexeme();
            updateCurrentChar();

            return greaterThan_or_Equal();
        }else
            return new Token( TokenId.op_greaterThan, lexeme, sourceManager.getLineNumber() );
    }

    private Token greaterThan_or_Equal(){
        return new Token( TokenId.op_greaterThanEqual, lexeme, sourceManager.getLineNumber() );
    }

    private Token lessThan() throws IOException {
        if( currentChar == '='){
            updateLexeme();
            updateCurrentChar();

            return lessThan_or_Equal();
        }else
            return new Token( TokenId.op_lessThan, lexeme, sourceManager.getLineNumber() );
    }

    private Token lessThan_or_Equal(){
        return new Token( TokenId.op_lessThanEqual, lexeme, sourceManager.getLineNumber());
    }

    private Token not_state() throws IOException {
        if( currentChar == '=' ){
            updateLexeme();
            updateCurrentChar();

            return not_state2();
        }else
            return new Token( TokenId.op_not, lexeme, sourceManager.getLineNumber() );
    }

    private Token not_state2(){
        return new Token( TokenId.op_notEquals, lexeme, sourceManager.getLineNumber() );
    }

    private Token assingment_state() throws IOException {
        if( currentChar == '=' ){
            updateLexeme();
            updateCurrentChar();

            return equals_state();
        }else
            return new Token( TokenId.op_assignment, lexeme, sourceManager.getLineNumber() );
    }

    private Token equals_state(){
        return new Token( TokenId.op_equals, lexeme, sourceManager.getLineNumber() );
    }

    private Token add_state() throws IOException {
        if (currentChar == '='){
            updateLexeme();
            updateCurrentChar();

            return assignmentAdition_state();
        }else
            return new Token( TokenId.op_add, lexeme, sourceManager.getLineNumber() );
    }

    private Token assignmentAdition_state(){
        return new Token( TokenId.op_assignmentAdition, lexeme, sourceManager.getLineNumber() );
    }

    private Token substract_state() throws IOException {
        if (currentChar == '='){
            updateLexeme();
            updateCurrentChar();

            return assignmentSubstraction_state();
        }else
            return new Token( TokenId.op_substract, lexeme, sourceManager.getLineNumber() );
    }

    private Token assignmentSubstraction_state(){
        return new Token( TokenId.op_assignmentSubstraction, lexeme, sourceManager.getLineNumber() );
    }

    private Token multiply_state(){
        return new Token( TokenId.op_multiply, lexeme, sourceManager.getLineNumber() );
    }

    private Token divide_state() throws LexicalException, IOException{
        if( currentChar == '/'){
            updateCurrentChar();

            return comment();
        }else if( currentChar == '*' ){
            multiline_comment_line = sourceManager.getPreviousLine();
            multiline_comment_row = sourceManager.getLineNumber();
            multiline_comment_column = sourceManager.getColumnNumber();

            updateCurrentChar();
            return multiline_comment_state();
        }
        return new Token( TokenId.op_divide, lexeme, sourceManager.getLineNumber() );
    }

    private Token comment() throws LexicalException, IOException {
        if( currentChar == '\n' || sourceManager.isEndOfFile( currentChar ) )
            return nextToken();
        else{
            updateCurrentChar();
            return comment();
        }
    }

    private Token multiline_comment_state() throws LexicalException, IOException {
        if( sourceManager.isEndOfFile( currentChar ) )
            throw new LexicalException( lexeme + "*", multiline_comment_row, multiline_comment_column, "Unterminated multiline comment", multiline_comment_line );
        else if ( currentChar == '\n' || currentChar == ' ' || currentChar == '\t' || currentChar != '*' ){
            updateCurrentChar();
            return multiline_comment_state();
        }else
            return close_multiline_comment();
    }

    private Token close_multiline_comment() throws LexicalException, IOException {
        if( currentChar == '*' ) {
            updateCurrentChar();
            return close_multiline_comment();
        }else if( currentChar == '/' ){
            updateCurrentChar();
            return nextToken();
        }else
            return multiline_comment_state();
    }

    private Token and_state() throws LexicalException, IOException {
        if( currentChar == '&' ){
            updateLexeme();
            updateCurrentChar();
            return new Token( TokenId.op_and, lexeme, sourceManager.getLineNumber() );
        }else
            throw new LexicalException( lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(),
                    "Unexpected symbol found after '&' -> Expected another '&' to form a valid '&&' operator.", sourceManager.getPreviousLine());
    }

    private Token or_state() throws LexicalException, IOException {
        if( currentChar == '|' ){
            updateLexeme();
            updateCurrentChar();
            return new Token( TokenId.op_or, lexeme, sourceManager.getLineNumber() );
        }else
            throw new LexicalException( lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(),
                    "Unexpected symbol found after '|' -> Expected another '|' to form a valid '||' operator.", sourceManager.getPreviousLine());
    }

    private Token mod_state() {
        return new Token( TokenId.op_mod, lexeme, sourceManager.getLineNumber() );
    }

    private Token strings_state() throws LexicalException, IOException {
        if( currentChar == '"' ) {
            updateLexeme();
            updateCurrentChar();
            return new Token(TokenId.literal_String, lexeme, sourceManager.getLineNumber());
        } else if ( currentChar == '\\' ) {
            updateLexeme();
            updateCurrentChar();
            return string_with_backslash();
        } else if ( currentChar == '\n' || sourceManager.isEndOfFile( currentChar ) )
            throw new LexicalException( lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "poorly closed String", sourceManager.getPreviousLine());
        else
            return string_with_elements();

    }

    private Token string_with_backslash() throws LexicalException, IOException {
        if( currentChar == '\\' ){
            updateLexeme();
            updateCurrentChar();
            return string_with_backslash();
        }else if( currentChar == '\n' || sourceManager.isEndOfFile( currentChar ) )
            throw new LexicalException( lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "invalid String", sourceManager.getPreviousLine());
        else{
            updateLexeme();
            updateCurrentChar();
            return string_with_elements();
        }
    }

    private Token string_with_elements() throws LexicalException, IOException {
        if( currentChar == '\\' ){
            updateLexeme();
            updateCurrentChar();
            return string_with_backslash();
        } else if( currentChar == '"' ) {
            updateLexeme();
            updateCurrentChar();
            return new Token(TokenId.literal_String, lexeme, sourceManager.getLineNumber());
        } else if( currentChar == '\n' || sourceManager.isEndOfFile( currentChar )  )
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), (sourceManager.getColumnNumber() - lexeme.length() + 1), "poorly closed String", sourceManager.getPreviousLine());
        else {
            updateLexeme();
            updateCurrentChar();
            return string_with_elements();
        }

    }

    private Token endOfFile(){
        return new Token( TokenId.EOF, lexeme, sourceManager.getLineNumber() );
    }

    public String getLexeme() {
        return lexeme;
    }

    public SourceManager getSourceManager() {
        return sourceManager;
    }

}
