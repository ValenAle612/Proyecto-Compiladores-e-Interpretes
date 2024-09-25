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
    private String currentClassName;
    private boolean possibly_a_builder = false;

    private final Set<TokenId> class_tokens = new HashSet<>(Set.of(TokenId.kw_class));
    private final Set<TokenId> primitiveType_tokens = new HashSet<>(Set.of(TokenId.kw_void, TokenId.kw_int,
                                                                            TokenId.kw_char, TokenId.kw_boolean));
    private final Set<TokenId> statement_tokens = new HashSet<>(Set.of(TokenId.ps_semicolon, TokenId.kw_this,
                                                                        TokenId.method_var_id, TokenId.kw_new,
                                                                        TokenId.ps_openParenthesis, TokenId.kw_var,
                                                                        TokenId.kw_return, TokenId.kw_if, TokenId.kw_while,
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
                                                                            TokenId.ps_openParenthesis, TokenId.kw_this,
                                                                            TokenId.kw_new, TokenId.class_id, TokenId.method_var_id,
                                                                            TokenId.kw_null, TokenId.kw_true, TokenId.kw_false,
                                                                            TokenId.literal_integer, TokenId.literal_char, TokenId.literal_String));
    private final Set<TokenId> binaryOperator_tokens = new HashSet<>(Set.of(TokenId.op_or, TokenId.op_and, TokenId.op_equals,
                                                                            TokenId.op_notEquals, TokenId.op_lessThan,
                                                                            TokenId.op_greaterThan, TokenId.op_lessThanEqual,
                                                                            TokenId.op_greaterThanEqual, TokenId.op_add,
                                                                            TokenId.op_substract, TokenId.op_multiply,
                                                                            TokenId.op_divide, TokenId.op_mod));
    private final Set<TokenId> expression_tokens = new HashSet<>(Set.of(TokenId.op_add, TokenId.op_substract, TokenId.op_not,
                                                                        TokenId.kw_null, TokenId.kw_true, TokenId.kw_false, TokenId.literal_integer,
                                                                        TokenId.literal_char, TokenId.literal_String, TokenId.kw_this,
                                                                        TokenId.kw_new, TokenId.kw_static, TokenId.ps_openParenthesis,
                                                                        TokenId.method_var_id, TokenId.class_id));
    private final Set<TokenId> type_tokens = new HashSet<>(Set.of(TokenId.kw_int, TokenId.kw_char, TokenId.kw_boolean, TokenId.class_id));
    private final Set<TokenId> classMember_tokens =new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id,
                                                                        TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int,
                                                                        TokenId.kw_public));
    private final Set<TokenId> assigmentType_tokens = new HashSet<>(Set.of(TokenId.op_assignment, TokenId.op_assignmentAdition,
                                                                            TokenId.op_assignmentSubstraction));

    public SyntaxAnalyzer( LexicalAnalyzer lexicalAnalyzer ) throws LexicalException, SyntaxException, IOException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        currentToken = lexicalAnalyzer.nextToken();

        init();
    }

    public void match(String expectedTokenDescription, TokenId expectedTokenID) throws SyntaxException, LexicalException, IOException {
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
            setCurrentClassName();
            match("class identifier", TokenId.class_id);
            inherits_from();
            match("{", TokenId.ps_openBrace);
            members_parsing();
            match("}", TokenId.ps_closeBrace);
        }else
            throw new SyntaxException("class", currentToken);
    }

    private void setCurrentClassName(){
        this.currentClassName = currentToken.getLexeme();
    }

    private void inherits_from() throws LexicalException, SyntaxException, IOException {
        if(TokenId.kw_extends == currentToken.getTokenType() ){
            match("extends", TokenId.kw_extends);
            match("class identifier", TokenId.class_id);
        }else{
            //ε
        }
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
        visibility();
        modifiers();
        type();
        method_attribute();
    }

    private void visibility() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_public == currentToken.getTokenType() ){
            match("public", TokenId.kw_public);
        }else{
            //ε
        }
    }

    private void modifiers() throws LexicalException, SyntaxException, IOException {
        if (TokenId.kw_static == currentToken.getTokenType()){
            match("static", TokenId.kw_static);
        }else{
            //ε
        }
    }

    private void method_attribute() throws LexicalException, SyntaxException, IOException {
        if( currentToken.getTokenType() == TokenId.ps_openParenthesis && possibly_a_builder ) {//is a builder
            formal_arguments();
            block();
        } else {//is a method or an attribute
            method_or_attribute_id();
        }
    }

    private void method_or_attribute_id() throws  LexicalException, SyntaxException, IOException {
        if( currentToken.getTokenType() == TokenId.method_var_id ){
            match("method or variable identifier", TokenId.method_var_id);
            method_or_variable();
        } else
            throw new SyntaxException("method or variable identifier", currentToken);

    }

    private void method_or_variable() throws LexicalException, SyntaxException, IOException {
        if (currentToken.getTokenType() == TokenId.ps_semicolon) {//is a variable
            match(";", TokenId.ps_semicolon);
        } else if (currentToken.getTokenType() == TokenId.ps_comma) {//other variable of the list
            attributes_parsing_factorized();
        } else if( currentToken.getTokenType() == TokenId.ps_openParenthesis ){//is a method
            formal_arguments();
            block();
        } else
            throw new SyntaxException("; | , | (", currentToken);
    }

    private void attributes_parsing() throws LexicalException, SyntaxException, IOException {
        if( TokenId.method_var_id == currentToken.getTokenType() ){
            match("method or variable identifier", TokenId.method_var_id);
            attributes_parsing_factorized();
        }else
            throw new SyntaxException("a method or variable identifier", currentToken);
    }

    private void attributes_parsing_factorized() throws LexicalException, SyntaxException, IOException {
        if (TokenId.ps_comma == currentToken.getTokenType()) {
            match(",", TokenId.ps_comma);
            attributes_parsing();
        } else if (TokenId.ps_semicolon == currentToken.getTokenType()){
            match(";", TokenId.ps_semicolon);
        }else
            throw new SyntaxException(", | ;", currentToken);
    }

    private void type()  throws LexicalException, SyntaxException, IOException {
        if( primitiveType_tokens.contains( currentToken.getTokenType() ) ){
            primitiveType();
        }else if( TokenId.class_id == currentToken.getTokenType() ){
            check_is_builder();
            match("class identifier", TokenId.class_id);
        }else
            throw new SyntaxException("a type", currentToken);
    }

    private void primitiveType() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_void == currentToken.getTokenType() ){
            match("void",TokenId.kw_void);
        }else if( TokenId.kw_int == currentToken.getTokenType() ){
            match("int",TokenId.kw_int);
        }else if( TokenId.kw_char == currentToken.getTokenType() ){
            match("char",TokenId.kw_char);
        }else if( TokenId.kw_boolean == currentToken.getTokenType() ){
            match("boolean",TokenId.kw_boolean);
        } else
            throw new SyntaxException("void | int | char | boolean", currentToken);
    }

    private void check_is_builder(){
        if( currentClassName.equals(currentToken.getLexeme()) )
            possibly_a_builder = true;
    }

    private void formal_arguments() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        formal_arguments_parsing_optional();
        match(")", TokenId.ps_closeParenthesis);
    }

    private void formal_arguments_parsing_optional() throws LexicalException, SyntaxException, IOException {
        if( type_tokens.contains( currentToken.getTokenType() ) ){
            formal_arguments_parsing();
        }else{
            //ε
        }
    }

    private void formal_arguments_parsing() throws LexicalException, SyntaxException, IOException {
        if( type_tokens.contains( currentToken.getTokenType() ) ){
            single_formal_argument();
            formal_arguments_factorized();
        }else
            throw new SyntaxException("a formal argument", currentToken);
    }

    private void single_formal_argument() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_void != currentToken.getTokenType() ) {
            type();
            match("method or variable identifier", TokenId.method_var_id);
        }else
            throw new SyntaxException("a valid type", currentToken);
    }

    private void formal_arguments_factorized() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_comma == currentToken.getTokenType() ){
            match(",", TokenId.ps_comma);
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
            local_variable();
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

    private void local_variable() throws LexicalException, SyntaxException, IOException {
        match("var", TokenId.kw_var);
        match("method or variable identifier",TokenId.method_var_id);
        match("=",TokenId.op_assignment);
        expression_parsing();
    }

    private void expression_parsing() throws LexicalException, SyntaxException, IOException {
        if( unaryExpressions_tokens.contains( currentToken.getTokenType() ) ){
            unary_expression();
            recursive_expression();
        }else
            throw new SyntaxException("an expression", currentToken);
    }

    private void unary_expression() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_add == currentToken.getTokenType() || TokenId.op_substract == currentToken.getTokenType() || TokenId.op_not == currentToken.getTokenType() ){
            unary_expression();
            operand();
        }else if( operand_tokens.contains( currentToken.getTokenType() ) ){
            operand();
        }else
            throw new SyntaxException("an unary expression", currentToken);
    }

    private void operand() throws LexicalException, SyntaxException, IOException {
        if( literal_tokens.contains( currentToken.getTokenType() ) ){
            literal();
        } else if( access_tokens.contains( currentToken.getTokenType() ) ) {
            access();
        } else
            throw new SyntaxException("an operand", currentToken);
    }

    private void literal() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_null == currentToken.getTokenType() ){
            match("null", TokenId.kw_null);
        } else if ( TokenId.literal_integer == currentToken.getTokenType() ) {
            match("literal integer", TokenId.literal_integer);
        } else if ( TokenId.literal_char == currentToken.getTokenType() ) {
            match("literal character", TokenId.literal_char);
        } else if ( TokenId.literal_String == currentToken.getTokenType() ) {
            match("literal String", TokenId.literal_String);
        } else if ( TokenId.kw_true == currentToken.getTokenType() ) {
            match("true", TokenId.kw_true);
        } else if ( TokenId.kw_false == currentToken.getTokenType() ){
            match("false", TokenId.kw_false);
        }else
            throw new SyntaxException("a literal", currentToken);
    }

    private void access() throws LexicalException, SyntaxException, IOException {
        if( primary_tokens.contains( currentToken.getTokenType() ) ){
            primary();
            optional_chain();
        }else
            throw new SyntaxException("an accesss", currentToken);
    }

    private void primary() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_this == currentToken.getTokenType() ){
            this_access();
        } else if ( TokenId.kw_new == currentToken.getTokenType() ) {
            constructor_access();
        } else if ( TokenId.class_id == currentToken.getTokenType() ) {
            static_method_access();
        } else if ( TokenId.ps_openParenthesis == currentToken.getTokenType() ) {
            parenthesized_expression();
        } else if ( TokenId.method_var_id == currentToken.getTokenType() ){
            match("method or variable identifier", currentToken.getTokenType() );
            primary_factorized();
        } else
            throw new SyntaxException("a primary", currentToken);

    }

    private void this_access() throws LexicalException, SyntaxException, IOException {
        match("this", TokenId.kw_this);
    }

    private void constructor_access() throws LexicalException, SyntaxException, IOException {
        match("new", TokenId.kw_new);
        match("class identifier", TokenId.class_id);
        match("(", TokenId.ps_openParenthesis);
        match(")", TokenId.ps_closeParenthesis);
    }

    private void static_method_access() throws LexicalException, SyntaxException, IOException {
        match("class identifier", TokenId.class_id);
        match(".", TokenId.ps_dot);
        match("method or variable identifier", TokenId.method_var_id);
        current_arguments();
    }

    private void parenthesized_expression() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
    }

    private void primary_factorized() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            current_arguments();
        }else{
            //ε
        }
    }

    private void optional_chain() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_dot == currentToken.getTokenType() ){
            match(".", TokenId.ps_dot);
            match("method or variable identifier", TokenId.method_var_id);
            optional_chain_method_or_variable();
        }
    }

    private void optional_chain_method_or_variable() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            current_arguments();
            optional_chain();
        }else optional_chain();
    }

    private void current_arguments() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        expressions_as_arguments();
        match(")", TokenId.ps_closeParenthesis);
    }

    private void expressions_as_arguments() throws LexicalException, SyntaxException, IOException {
        if( expressionStart_token.contains( currentToken.getTokenType() ) ){
            expressions_as_arguments_factorized();
        }else{
            //ε
        }
    }

    private void expressions_as_arguments_factorized() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_comma == currentToken.getTokenType() ){
            match(",", TokenId.ps_comma);
            expressions_as_arguments();
        }else{
            //ε
        }
    }

    private void recursive_expression() throws LexicalException, SyntaxException, IOException {
        if( binaryOperator_tokens.contains( currentToken.getTokenType() ) ){
            binary_operator();
            unary_expression();
            recursive_expression();
        }else{
            //ε
        }
    }

    private void binary_operator() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_or == currentToken.getTokenType() ){
            match("||", TokenId.op_or);
        } else if ( TokenId.op_and == currentToken.getTokenType() ){
            match("&&", TokenId.op_and);
        } else if ( TokenId.op_equals == currentToken.getTokenType() ){
            match("==", TokenId.op_equals);
        } else if ( TokenId.op_notEquals == currentToken.getTokenType() ) {
            match("!=", TokenId.op_notEquals);
        } else if ( TokenId.op_lessThan == currentToken.getTokenType() ) {
            match("<", TokenId.op_lessThan);
        } else if ( TokenId.op_lessThanEqual == currentToken.getTokenType() ) {
            match("<=", TokenId.op_lessThanEqual);
        } else if ( TokenId.op_greaterThan == currentToken.getTokenType() ) {
            match(">", TokenId.op_greaterThan);
        } else if ( TokenId.op_greaterThanEqual == currentToken.getTokenType() ) {
            match(">=", TokenId.op_greaterThanEqual);
        } else if ( TokenId.op_add == currentToken.getTokenType() ){
            match("+", TokenId.op_add);
        } else if ( TokenId.op_substract == currentToken.getTokenType() ) {
            match("-", TokenId.op_substract);
        } else if ( TokenId.op_multiply == currentToken.getTokenType() ) {
            match("*", TokenId.op_multiply);
        } else if ( TokenId.op_divide == currentToken.getTokenType() ) {
            match("/", TokenId.op_divide);
        } else if ( TokenId.op_mod == currentToken.getTokenType() ) {
            match("%", TokenId.op_mod);
        } else
            throw new SyntaxException("a binary operator", currentToken);
    }

    private void return_statement() throws LexicalException, SyntaxException, IOException {
        match("return", TokenId.kw_return);
        return_expression();
    }

    private void return_expression() throws LexicalException, SyntaxException, IOException {
        if( expression_tokens.contains( currentToken.getTokenType() ) ){
            expression_parsing();
        }else{
            //ε
        }
    }

    private void if_statement() throws LexicalException, SyntaxException, IOException {
        match("if", TokenId.kw_if);
        match("(", TokenId.ps_openParenthesis);
        expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        statement();
        else_statement();
    }

    private void else_statement() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_else == currentToken.getTokenType() ){
            match("else", TokenId.kw_else);
            statement();
        }else{
            //ε
        }
    }

    private void while_statement() throws LexicalException, SyntaxException, IOException {
        match("while", TokenId.kw_while);
        match("(", TokenId.ps_openParenthesis);
        expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        statement();
    }

    private void assignment_statement_or_call() throws LexicalException, SyntaxException, IOException {
        if( assigmentType_tokens.contains( currentToken.getTokenType() ) ){
            assignment_type();
            expression_parsing();
        }else{
            //ε
        }
    }

    private void assignment_type() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_assignment == currentToken.getTokenType() ){
            match("=", TokenId.op_assignment);
        } else if ( TokenId.op_assignmentAdition == currentToken.getTokenType() ) {
            match("+=", TokenId.op_assignmentAdition);
        } else if ( TokenId.op_assignmentSubstraction == currentToken.getTokenType() ) {
            match("-=", TokenId.op_assignmentSubstraction);
        }
    }

}
