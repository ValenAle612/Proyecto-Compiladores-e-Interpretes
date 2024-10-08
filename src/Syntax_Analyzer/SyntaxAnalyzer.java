package Syntax_Analyzer;

import Lexical_Analyzer.LexicalAnalyzer;
import Lexical_Analyzer.LexicalException;
import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class SyntaxAnalyzer {

    LexicalAnalyzer lexicalAnalyzer;
    Token currentToken;

    private Token current_class;

    private final Set<TokenId> class_tokens = new HashSet<>(Set.of(TokenId.kw_class));
    private final Set<TokenId> primitiveType_tokens = new HashSet<>(Set.of(TokenId.kw_void, TokenId.kw_int,
            TokenId.kw_char, TokenId.kw_boolean));
    private final Set<TokenId> statement_tokens = new HashSet<>(Set.of(TokenId.ps_semicolon, TokenId.kw_this,
            TokenId.method_var_id, TokenId.kw_new,
            TokenId.ps_openParenthesis, TokenId.kw_var,
            TokenId.kw_int, TokenId.kw_char, TokenId.kw_boolean,
            TokenId.kw_return, TokenId.kw_if, TokenId.kw_while,
            TokenId.kw_switch, TokenId.ps_openBrace));
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
            TokenId.kw_public, TokenId.kw_private));
    private final Set<TokenId> switch_tokens = new HashSet<>(Set.of(TokenId.kw_case, TokenId.kw_default) );

    private final Set<TokenId> assigmentType_tokens = new HashSet<>(Set.of(TokenId.op_assignment, TokenId.op_assignmentAdition,
            TokenId.op_assignmentSubstraction));

    public SyntaxAnalyzer( LexicalAnalyzer lexicalAnalyzer ) throws LexicalException, SyntaxException, IOException, SemanticException {
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

    public void init() throws LexicalException, SyntaxException, IOException, SemanticException {
        class_list();
        match("EOF",TokenId.EOF);
    }

    public void class_list() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( class_tokens.contains( currentToken.getTokenType() ) ){
            class_parsing();
            class_list();
        }else{
            //ε
        }
    }

    public void class_parsing() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( TokenId.kw_class == currentToken.getTokenType() ){
            match("class", TokenId.kw_class);
            Token token = currentToken;
            current_class = currentToken;
            match("class identifier", TokenId.class_id);
            ConcreteClass concreteClass = new ConcreteClass(token);
            SymbolTable.current_class = concreteClass;
            Token inherited_class = inherits_from();
            concreteClass.setInherit_class_token(inherited_class);
            SymbolTable.getInstance().save_class(concreteClass, token.getLexeme());
            match("{", TokenId.ps_openBrace);
            members_parsing();
            match("}", TokenId.ps_closeBrace);
        }else
            throw new SyntaxException("class", currentToken);
    }

    private Token inherits_from() throws LexicalException, SyntaxException, IOException {
        if(TokenId.kw_extends == currentToken.getTokenType() ){
            match("extends", TokenId.kw_extends);
            Token token = currentToken;
            match("class identifier", TokenId.class_id);
            return token;
        }else{
            return new Token(TokenId.class_id, "Object", 0);
        }
    }

    private void members_parsing() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( classMember_tokens.contains( currentToken.getTokenType() ) ){
            member();
            members_parsing();
        }else{
            //ε
        }
    }

    private void member() throws LexicalException, SyntaxException, IOException, SemanticException {
        TokenId visibility_token = visibility();
        TokenId static_token = static_optional();
        Token token;
        Type type;

        if (TokenId.class_id == currentToken.getTokenType()){

            token = currentToken;
            match("class identifier", TokenId.class_id);
            type = null;

            if(currentToken.getTokenType() == TokenId.ps_openParenthesis && !token.getLexeme().equals(current_class.getLexeme()))
                throw new SyntaxException("method or variable identifier", currentToken);
            else if(currentToken.getTokenType() != TokenId.ps_openParenthesis){//its not a builder
                type = new ClassType(token);
                token = currentToken;
                match("method or variable identifier", TokenId.method_var_id);
            }

        }else{
            type = method_var_type();
            token = currentToken;
            match("method or variable identifier", TokenId.method_var_id);
        }

        if(TokenId.ps_openParenthesis == currentToken.getTokenType()){//is a method
            method(token, static_token, type);
        } else if (TokenId.ps_semicolon == currentToken.getTokenType() || TokenId.ps_comma == currentToken.getTokenType()) {//is an attribute
            attribute(token, visibility_token, static_token, type);
        } else
            throw new SyntaxException("( | ; | ,", currentToken);

    }

    private void method(Token token, TokenId static_token, Type type) throws LexicalException, SyntaxException, IOException, SemanticException {
        Method method = new Method(new HashMap<String, Parameter>(), new ArrayList<Parameter>(), token, static_token, type);
        SymbolTable.current_method = method;
        formal_arguments();
        SymbolTable.current_class.save_method(method);
        block();
    }

    private void attribute(Token token, TokenId visibility_token, TokenId static_token, Type type) throws LexicalException, SyntaxException, IOException, SemanticException {
        Attribute attribute = new Attribute(token, visibility_token, static_token, type);
        System.out.println(" ATRIBUTO "+attribute.getAttribute_token().getLexeme());
        SymbolTable.current_class.save_attribute(attribute);
        SymbolTable.current_attribute = attribute;
        attributes_list_factorized(visibility_token, static_token, type);
        match(";", TokenId.ps_semicolon);
    }

    private TokenId static_optional() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.kw_static == currentToken.getTokenType()){
            match("static", TokenId.kw_static);
            return TokenId.kw_static;
        }else{
            return null;
        }
    }

    private Type method_var_type() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(type_tokens.contains(currentToken.getTokenType())){
            return type();
        }else if(TokenId.kw_void == currentToken.getTokenType()){
            match("void", TokenId.kw_void);
            return new VoidType();
        }else
            throw new SyntaxException("a method type", currentToken);
    }

    private TokenId visibility() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_public == currentToken.getTokenType() ){
            match("public", TokenId.kw_public);
            return TokenId.kw_public;
        }else if ( TokenId.kw_private == currentToken.getTokenType() ){
            match("private", TokenId.kw_private);
            return TokenId.kw_private;
        }else{
            return null;
        }
    }

    private void attributes_list(TokenId tokenId, TokenId static_token, Type type) throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.method_var_id == currentToken.getTokenType()){
            Token token = currentToken;
            match("method or variable identifier", currentToken.getTokenType());
            Attribute attribute = new Attribute(token, tokenId, static_token, type);
            SymbolTable.current_class.save_attribute(attribute);
            SymbolTable.current_attribute = attribute;
            attributes_list_factorized(tokenId, static_token, type);
        }else
            throw new SyntaxException("a method or variable identifier", currentToken);
    }

    private void attributes_list_factorized(TokenId tokenId, TokenId static_token, Type type) throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.ps_comma == currentToken.getTokenType()){
            match(",", TokenId.ps_comma);
            attributes_list(tokenId, static_token, type);
        }else{
            //ε
        }
    }

    private ConcreteType type()  throws LexicalException, SyntaxException, IOException {
        if( primitiveType_tokens.contains( currentToken.getTokenType() ) ){
            return primitiveType();
        }else if( TokenId.class_id == currentToken.getTokenType() ){
            Token token = currentToken;
            match("class identifier", TokenId.class_id);
            return new ClassType(token);
        }else
            throw new SyntaxException("a type", currentToken);
    }

    private ConcreteType primitiveType() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_int == currentToken.getTokenType() ){
            match("int",TokenId.kw_int);
            return new IntType();
        }else if( TokenId.kw_char == currentToken.getTokenType() ){
            match("char",TokenId.kw_char);
            return new CharType();
        }else if( TokenId.kw_boolean == currentToken.getTokenType() ){
            match("boolean",TokenId.kw_boolean);
            return new BooleanType();
        } else
            throw new SyntaxException("int | char | boolean", currentToken);
    }

    private void formal_arguments() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("(", TokenId.ps_openParenthesis);
        formal_arguments_parsing_optional();
        match(")", TokenId.ps_closeParenthesis);
    }

    private void formal_arguments_parsing_optional() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( type_tokens.contains( currentToken.getTokenType() ) ){
            formal_arguments_parsing();
        }else{
            //ε
        }
    }

    private void formal_arguments_parsing() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( type_tokens.contains( currentToken.getTokenType() ) ){
            single_formal_argument();
            formal_arguments_factorized();
        }else
            throw new SyntaxException("a formal argument", currentToken);
    }

    private void single_formal_argument() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( type_tokens.contains(currentToken.getTokenType())) {
            ConcreteType type = type();
            Token token = currentToken;
            match("method or variable identifier", TokenId.method_var_id);
            Parameter parameter = new Parameter(token, type);
            SymbolTable.current_method.save_parameter(token.getLexeme(), parameter);
        }else
            throw new SyntaxException("a valid type", currentToken);
    }

    private void formal_arguments_factorized() throws LexicalException, SyntaxException, IOException, SemanticException {
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
        } else if ( TokenId.kw_switch == currentToken.getTokenType() ){
            switch_statement();
        }else  if ( access_tokens.contains( currentToken.getTokenType() ) ) {
            access();
            assignment_statement_or_call();
            match(";",TokenId.ps_semicolon);
        }else if ( TokenId.ps_openBrace == currentToken.getTokenType() ) {
            block();
        } else if ( unaryExpressions_tokens.contains( currentToken.getTokenType() )){
            unary_expression();
            recursive_expression();
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
            unary_operand();
            operand();
        }else if( operand_tokens.contains( currentToken.getTokenType() ) ){
            operand();
        }else
            throw new SyntaxException("an unary expression", currentToken);
    }

    private void unary_operand() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_add == currentToken.getTokenType() ){
            match( "+", TokenId.op_add );
        }else if( TokenId.op_substract == currentToken.getTokenType() ){
            match( "-", TokenId.op_substract );
        }else if( TokenId.op_not == currentToken.getTokenType() ){
            match( "!", TokenId.op_not );
        }
        else
            throw new SyntaxException( "an unary operand", currentToken );
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
            builder_access();
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

    private void builder_access() throws LexicalException, SyntaxException, IOException {
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
        } else {
            //ε
        }
    }

    private void optional_chain_method_or_variable() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            current_arguments();
            optional_chain();
        } else
            optional_chain();
    }

    private void current_arguments() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        expressions_as_arguments_optional();
        match(")", TokenId.ps_closeParenthesis);
    }

    private void expressions_as_arguments_optional() throws LexicalException, SyntaxException, IOException {
        if( expressionStart_token.contains( currentToken.getTokenType() ) ){
            expressions_as_arguments();
        }else{
            //ε
        }
    }

    private void expressions_as_arguments() throws LexicalException, SyntaxException, IOException {
        if(expression_tokens.contains( currentToken.getTokenType() ) ){
            expression_parsing();
            expressions_as_arguments_factorized();
        }else
            throw new SyntaxException("an expression list", currentToken);
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

    private void switch_statement() throws LexicalException, SyntaxException, IOException {
        match("switch", TokenId.kw_switch);
        match("(", TokenId.ps_openParenthesis);
        expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        match("{", TokenId.ps_openBrace);
        switch_list_statement();
        match("}", TokenId.ps_closeBrace);
    }

    private void switch_list_statement() throws LexicalException, SyntaxException, IOException {
        if( switch_tokens.contains( currentToken.getTokenType() ) ) {
            switch_list_statement_parsing();
            switch_list_statement();
        }else{
            //ε
        }
    }

    private void switch_list_statement_parsing() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_case == currentToken.getTokenType() ){
            match("case", TokenId.kw_case);
            literal();
            match(":", TokenId.ps_colon);
            case_optional_statement();
        }else if( TokenId.kw_default == currentToken.getTokenType() ){
            match("default", TokenId.kw_default);
            match(":", TokenId.ps_colon);
            statement();
        }else{
            //ε
        }
    }

    private void case_optional_statement() throws LexicalException, SyntaxException, IOException {
        if(statement_tokens.contains( currentToken.getTokenType() ) ){
            statement();
        }else{
            //ε
        }
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
