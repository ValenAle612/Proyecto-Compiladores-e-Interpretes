package Syntax_Analyzer;

import Lexical_Analyzer.LexicalAnalyzer;
import Lexical_Analyzer.LexicalException;
import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

public class SyntaxAnalyzer {

    LexicalAnalyzer lexicalAnalyzer;
    Token currentToken;

    private final Set<TokenId> class_tokens = new HashSet<>(Set.of(TokenId.kw_class));
    private final Set<TokenId> attribute_tokens = new HashSet<>(Set.of(TokenId.kw_public));
    private final Set<TokenId> primitiveType_tokens = new HashSet<>(Set.of(TokenId.kw_int,
                                                                            TokenId.kw_char, TokenId.kw_boolean));
    private final Set<TokenId> statement_tokens = new HashSet<>(Set.of(TokenId.ps_semicolon, TokenId.kw_this,
                                                                        TokenId.method_var_id, TokenId.kw_new,
                                                                        TokenId.ps_openParenthesis, TokenId.kw_var,
                                                                        TokenId.kw_return, TokenId.kw_if, TokenId.kw_while
                                                                            TokenId.ps_openBrace));
    private final Set<TokenId> access_tokens = new HashSet<>(Set.of(TokenId.kw_this, TokenId.kw_new, TokenId.class_id,
                                                                        TokenId.ps_openParenthesis, TokenId.method_var_id));
    private final Set<TokenId> unaryExpressions_tokens = new HashSet<>(Set.of(TokenId.op_add, TokenId.op_substract,
                                                                                TokenId.op_not, TokenId.kw_null, TokenId.kw_true,
                                                                                TokenId.kw_false, TokenId.literal_integer, TokenId.literal_char,
                                                                                TokenId.literal_String, TokenId.kw_this, TokenId.method_var_id,
                                                                                TokenId.kw_new, TokenId.class_id, TokenId.ps_openParenthesis ));
    private final Set<TokenId> operand_tokens = new HashSet<>(Set.of(TokenId.kw_null, TokenId.kw_true, TokenId.kw_false,
                                                                                TokenId.literal_integer, TokenId.literal_char,
                                                                                TokenId.literal_String, TokenId.kw_this,
                                                                                TokenId.kw_new, TokenId.class_id, TokenId.ps_openParenthesis,
                                                                                TokenId.method_var_id));
    private final Set<TokenId> literal_tokens = new HashSet<>(Set.of(TokenId.kw_null, TokenId.kw_true, TokenId.kw_false, TokenId.literal_integer,
                                                                        TokenId.literal_char, TokenId.literal_String));
    private final Set<TokenId> primary_tokens = new HashSet<>(Set.of(TokenId.kw_this, TokenId.kw_new, TokenId.class_id,
                                                                        TokenId.ps_openParenthesis, TokenId.method_var_id));
    private final Set<TokenId> expressionStart_token = new HashSet<>(Set.of(TokenId.op_add, TokenId.op_substract, TokenId.op_not,
                                                                            TokenId.ps_openParenthesis, TokenId.kw_this
                                                                            TokenId.kw_new, TokenId.class_id, TokenId.method_var_id,
                                                                            TokenId.kw_null, TokenId.kw_true, TokenId.kw_false,
                                                                            TokenId.literal_integer, TokenId.literal_char, TokenId.literal_String));
    private final Set<TokenId> binaryOperator_tokens = new HashSet<>(Set.of(TokenId.op_or, TokenId.op_and, TokenId.op_equals,
                                                                            TokenId.op_notEquals, TokenId.op_lessThan,
                                                                            TokenId.op_greaterThan, TokenId.op_lessThanEqual,
                                                                            TokenId.op_add, TokenId.op_substract, TokenId.op_multiply,
                                                                            TokenId.op_divide, TokenId.op_mod));
    private final Set<TokenId> expression_tokens = new HashSet<>(Set.of(TokenId.op_add, TokenId.op_substract, TokenId.op_not,
                                                                        TokenId.kw_null, TokenId.kw_true, TokenId.kw_false, TokenId.literal_integer,
                                                                        TokenId.literal_char, TokenId.literal_String, TokenId.kw_this,
                                                                        TokenId.kw_new, TokenId.kw_static, TokenId.ps_openParenthesis,
                                                                        TokenId.method_var_id, TokenId.class_id));
    private final Set<TokenId> type_tokens = new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id));
    private final Set<TokenId> methodHeader_tokens = new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id,
                                                                          TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int));
    private final Set<TokenId> classMember_tokens =new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id,
                                                                        TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int,
                                                                        TokenId.kw_public));
    private final Set<TokenId> method_tokens = new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id,
                                                                    TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int));
    private final Set<TokenId> assigmentType_tokens = new HashSet<>(Set.of(TokenId.op_assignment, TokenId.op_assignmentAdition,
                                                                            TokenId.op_assignmentSubstraction));

    public SyntaxAnalyzer( LexicalAnalyzer lexicalAnalyzer ) throws LexicalException, SyntaxException, IOException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        currentToken = lexicalAnalyzer.nextToken();

        init();
    }

    private void match(String expectedTokenDescription, TokenId expectedTokenID) throws SyntaxException, LexicalException, IOException {
        if ( currentToken.getTokenType() != expectedTokenID )
            throw new SyntaxException(expectedTokenDescription, currentToken);
        else
            currentToken = lexicalAnalyzer.nextToken();
    }

    public void init() throws LexicalException, SyntaxException, IOException {
        class_list();
        match("EOF",TokenId.EOF);
    }

    public void class_list() throws LexicalException, SyntaxException, IOException {
        if( class_tokens.contains( currentToken.getTokenType() ) ){
            class_parsing();
            class_list();
        }else{
            //ε
        }
    }

    public void class_parsing() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_class == currentToken.getTokenType() ){
            match("class", TokenId.kw_class);
            match("class id", TokenId.class_id);
            match("{", TokenId.ps_openBrace);
            members_parsing();
            match("}", TokenId.ps_closeBrace);
        }else
            throw new SyntaxException("class", currentToken);
    }

    private void members_parsing() throws LexicalException, SyntaxException, IOException {
        if( classMember_tokens.contains( currentToken.getTokenType() ) ){
            member();
            members_parsing();
        }else{
            //ε
        }
    }

    private void member() throws LexicalException, SyntaxException, IOException {
        if( attribute_tokens.contains( currentToken.getTokenType() ) ) {
            attribute();
        }else if( method_tokens.contains( currentToken.getTokenType() ) ) {
            method();
        }else
            throw new SyntaxException("public | static", currentToken);
    }

    private void attribute() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_public == currentToken.getTokenType() ){
            visibility();
            type();
            attributes_parsing();
            match(";", TokenId.ps_semicolon);
        }else
            throw new SyntaxException("an attribute", currentToken);
    }

    private void visibility() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_public == currentToken.getTokenType() ){
            match("public", TokenId.kw_public);
        }else
            throw new SyntaxException("public", currentToken);
    }

    private void attributes_parsing() throws LexicalException, SyntaxException, IOException {
        if( TokenId.method_var_id == currentToken.getTokenType() ){
            match("method or variable identifier", currentToken.getTokenType());
            attributes_parsing_factorized();
        }else
            throw new SyntaxException("a method or variable identifier", currentToken);
    }

    private void attributes_parsing_factorized() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_colon == currentToken.getTokenType() ){
            match(",",TokenId.ps_colon);
            attributes_parsing();
        }else{
            //ε
        }
    }

    private void method() throws LexicalException, SyntaxException, IOException {
        method_header();
        block();
    }

    private void method_header() throws LexicalException, SyntaxException, IOException {
        if( methodHeader_tokens.contains( currentToken.getTokenType()) ){
            static_method();
            method_type();
            match("class or method identifier", TokenId.method_var_id);
            formal_arguments();
        }else
            throw new SyntaxException("method header", currentToken);
    }

    private void static_method() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_static == currentToken.getTokenType() ){
            match("static", TokenId.kw_static);
        }else{
            //ε
        }
    }

    private void method_type() throws LexicalException, SyntaxException, IOException {
        if( type_tokens.contains( currentToken.getTokenType() ) ){
            type();
        }else if( TokenId.kw_void == currentToken.getTokenType() ){
            match("void", TokenId.kw_void );
        }else
            throw new SyntaxException("method type", currentToken);
    }

    private void type()  throws LexicalException, SyntaxException, IOException {
        if( primitiveType_tokens.contains( currentToken.getTokenType() ) ){
            primitiveType();
        }else if( TokenId.class_id == currentToken.getTokenType() ){
            match("class identifier", TokenId.class_id);
        }else
            throw new SyntaxException("a type", currentToken);
    }

    private void primitiveType() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_int == currentToken.getTokenType() ){
            match("int",TokenId.kw_int);
        }else if( TokenId.kw_char == currentToken.getTokenType() ){
            match("char",TokenId.kw_char);
        }else if( TokenId.kw_boolean == currentToken.getTokenType() ){
            match("boolean",TokenId.kw_boolean);
        } else
            throw new SyntaxException("int | char | boolean", currentToken);
    }

    private void formal_arguments() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        formal_arguments_parsing();
        match(")", TokenId.ps_closeParenthesis);
    }

    private void formal_arguments_parsing() throws LexicalException, SyntaxException, IOException {
        if( type_tokens.contains( currentToken.getTokenType() ) ){
            formal_arguments_parsing();
        }else{
            //ε
        }
    }

    private void block() throws LexicalException, SyntaxException, IOException {
        match("{", TokenId.ps_openBrace);
        statement_parsing();
        match("}",TokenId.ps_closeBrace);
    }

    private void statement_parsing() throws LexicalException, SyntaxException, IOException {
        if( statement_tokens.contains( currentToken.getTokenType() ) ){
            statement();
            statement_parsing();
        }else{
            //ε
        }
    }

    private void statement() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_semicolon == currentToken.getTokenType() ){
            match(";", TokenId.ps_semicolon);
        } else if ( TokenId.kw_var == currentToken.getTokenType() ){
            locar_variable();
            match(";",TokenId.ps_semicolon);
        } else if ( TokenId.kw_return == currentToken.getTokenType() ){
            return_statement();
            match(";",TokenId.ps_semicolon);
        } else if ( TokenId.kw_if == currentToken.getTokenType() ){
            if_statement();
        } else if ( TokenId.kw_while == currentToken.getTokenType() ){
            while_statement();
        } else if ( TokenId.ps_openBrace == currentToken.getTokenType() ) {
            block();
        } else if ( access_tokens.contains( currentToken.getTokenType() ) ) {
            access();
            assignment_statement_or_call();
            match(";",TokenId.ps_semicolon);
        } else
            throw new SyntaxException("a statement", currentToken);

    }


}
