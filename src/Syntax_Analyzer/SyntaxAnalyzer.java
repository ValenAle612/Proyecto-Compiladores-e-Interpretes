package Syntax_Analyzer;

import Lexical_Analyzer.LexicalAnalyzer;
import Lexical_Analyzer.LexicalException;
import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.*;
import Symbol_Table.Nodes.Access.*;
import Symbol_Table.Nodes.Expression.*;
import Symbol_Table.Nodes.Literal.*;
import Symbol_Table.Nodes.Statement.*;
import Symbol_Table.Types.*;

import java.io.IOException;
import java.util.*;

public class SyntaxAnalyzer {

    LexicalAnalyzer lexicalAnalyzer;
    Token currentToken;

    private Token current_class, last_call;

    private final Set<TokenId> class_tokens = new HashSet<>(Set.of(TokenId.kw_class));
    private final Set<TokenId> primitiveType_tokens = new HashSet<>(Set.of(TokenId.kw_void, TokenId.kw_int,
            TokenId.kw_char, TokenId.kw_boolean));
    private final Set<TokenId> statement_tokens = new HashSet<>(Set.of(TokenId.ps_semicolon, TokenId.kw_this,
            TokenId.class_id, TokenId.method_var_id, TokenId.kw_new,
            TokenId.ps_openParenthesis, TokenId.kw_var,
            TokenId.kw_int, TokenId.kw_char, TokenId.kw_boolean,
            TokenId.kw_return, TokenId.kw_if, TokenId.kw_while,
            TokenId.kw_switch, TokenId.ps_openBrace));
    private final Set<TokenId> access_tokens = new HashSet<>(Set.of(TokenId.kw_this, TokenId.kw_new, TokenId.class_id,
            TokenId.ps_openParenthesis, TokenId.method_var_id));
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

    private void class_list() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( class_tokens.contains( currentToken.getTokenType() ) ){
            class_parsing();
            class_list();
        }else{
            //ε
        }
    }

    private void class_parsing() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( TokenId.kw_class == currentToken.getTokenType() ){
            concrete_class();
        }else
            throw new SyntaxException("class", currentToken);
    }

    private void concrete_class() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("class", TokenId.kw_class);
        Token token = currentToken;
        match("class identifier", TokenId.class_id);
        ConcreteClass concreteClass = new ConcreteClass(token);
        SymbolTable.current_class = concreteClass;
        Token inherited_class = inherits_from();
        concreteClass.setInherit_class_token(inherited_class);
        SymbolTable.getInstance().save_class(concreteClass, token.getLexeme());
        match("{", TokenId.ps_openBrace);
        members_parsing();
        match("}", TokenId.ps_closeBrace);
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
            attribute(token, visibility_token, static_token, (ConcreteType) type);
        } else
            throw new SyntaxException("( | ; | ,", currentToken);

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

    private TokenId static_optional() throws LexicalException, SyntaxException, IOException {
        if(TokenId.kw_static == currentToken.getTokenType()){
            match("static", TokenId.kw_static);
            return TokenId.kw_static;
        }else{
            return null;
        }
    }

    private Type method_var_type() throws LexicalException, SyntaxException, IOException {
        if(type_tokens.contains(currentToken.getTokenType())){
            return type();
        }else if(TokenId.kw_void == currentToken.getTokenType()){
            match("void", TokenId.kw_void);
            return new VoidType();
        }else
            throw new SyntaxException("a method type", currentToken);
    }

    private void attribute(Token token, TokenId visibility, TokenId static_token , ConcreteType type) throws LexicalException, SyntaxException, IOException, SemanticException {
        Attribute attribute = new Attribute(token, visibility, static_token, type);
        SymbolTable.current_class.save_attribute(attribute);
        attribute.setClass_that_contains_the_attribute(SymbolTable.current_class.getToken());
        SymbolTable.current_attribute = attribute;
        attributes_list_factorized(visibility, static_token, type);
        match(";", TokenId.ps_semicolon);

    }

    private void attributes_list(TokenId visibility, TokenId static_token , ConcreteType type) throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.method_var_id == currentToken.getTokenType()){
            Token token = currentToken;
            match("method or variable identifier", currentToken.getTokenType());
            Attribute attribute = new Attribute(token, visibility, static_token, type);
            SymbolTable.current_class.save_attribute(attribute);
            attribute.setClass_that_contains_the_attribute(SymbolTable.current_class.getToken());
            SymbolTable.current_attribute = attribute;
            attributes_list_factorized(visibility, static_token, type);
        }else
            throw new SyntaxException("a method or variable identifier", currentToken);
    }

    private void attributes_list_factorized(TokenId visibility, TokenId static_token , ConcreteType type) throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.ps_comma == currentToken.getTokenType()){
            match(",", TokenId.ps_comma);
            attributes_list(visibility, static_token, type);
        }else{
            //ε
        }
    }

    private void method(Token token, TokenId static_token, Type type) throws LexicalException, SyntaxException, IOException, SemanticException {
        method_header(token, static_token, type);
        Method method = SymbolTable.current_method;
        method.setAssociated_class(SymbolTable.current_class.getToken());
        BlockNode block = block();
        method.insert_block(block);
    }

    private void method_header(Token token, TokenId static_token, Type type) throws LexicalException, SyntaxException, IOException, SemanticException {
        Method method = new Method(new HashMap<>(), new ArrayList<>(), token,
                static_token, type, SymbolTable.current_class.getToken());
        SymbolTable.current_method = method;
        formal_arguments();
        SymbolTable.current_class.save_method(method);
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

    private BlockNode block() throws LexicalException, SyntaxException, SemanticException, IOException {
        match("{", TokenId.ps_openBrace);
        BlockNode blockNode = new BlockNode();
        statement_parsing(blockNode);
        match("}",TokenId.ps_closeBrace);
        return blockNode;
    }

    private void statement_parsing(BlockNode block) throws LexicalException, SyntaxException, SemanticException, IOException {
        if( statement_tokens.contains( currentToken.getTokenType() ) ){
            StatementNode statementNode = statement();
            block.insert_statement(statementNode);
            statement_parsing(block);
        }else{
            //ε
        }
    }

    private StatementNode statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        if( TokenId.ps_semicolon == currentToken.getTokenType() ){
            match(";", TokenId.ps_semicolon);
            return new EmptyStatementNode();
        } else if ( TokenId.kw_var == currentToken.getTokenType() ){
            StatementNode variable_node = local_variable();
            match(";",TokenId.ps_semicolon);
            return variable_node;
        } else if ( TokenId.kw_return == currentToken.getTokenType() ){
            StatementNode return_node = return_statement();
            match(";",TokenId.ps_semicolon);
            return return_node;
        } else if ( TokenId.kw_if == currentToken.getTokenType() ){
            return if_statement();
        } else if ( TokenId.kw_while == currentToken.getTokenType() ){
            return while_statement();
        } else if ( TokenId.kw_switch == currentToken.getTokenType() ){
            return switch_statement();
        }else  if ( access_tokens.contains( currentToken.getTokenType() ) ) {
            last_call = null;
            AccessNode accessNode = access();

            if(currentToken.getTokenType() == TokenId.ps_semicolon && last_call == null)
                throw new SemanticException(currentToken, "Statement expected; isolated expressions are not allowed");

            StatementNode statementNode = assignment_statement_or_call(accessNode);
            match(";",TokenId.ps_semicolon);
            return statementNode;
        }else if ( TokenId.ps_openBrace == currentToken.getTokenType() ) {
            return block();
        }else
            throw new SyntaxException("a statement", currentToken);

    }

    private LocalVarNode local_variable() throws LexicalException, SyntaxException, IOException {
        match("var", TokenId.kw_var);
        Token token = currentToken;
        LocalVarNode localVarNode = new LocalVarNode(token);
        match("method or variable identifier",TokenId.method_var_id);
        match("=",TokenId.op_assignment);
        ExpressionNode expressionNode = expression_parsing();
        localVarNode.setExpressionNode(expressionNode);

        return localVarNode;
    }

    private ExpressionNode expression_parsing() throws LexicalException, SyntaxException, IOException {
        ExpressionNode left_node = unary_expression();
        return recursive_expression(left_node);
    }

    private ExpressionNode unary_expression() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_add == currentToken.getTokenType() || TokenId.op_substract == currentToken.getTokenType() || TokenId.op_not == currentToken.getTokenType() ){
            Token token = unary_operand();
            OperandNode operandNode = operand();
            return new UnaryExpressionNode(token, operandNode);
        }else if( operand_tokens.contains( currentToken.getTokenType() ) ){
            return operand();
        }else
            throw new SyntaxException("an unary expression", currentToken);
    }

    private Token unary_operand() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;

        if( TokenId.op_add == currentToken.getTokenType() ){
            match( "+", TokenId.op_add );
        }else if( TokenId.op_substract == currentToken.getTokenType() ){
            match( "-", TokenId.op_substract );
        }else if( TokenId.op_not == currentToken.getTokenType() ){
            match( "!", TokenId.op_not );
        }
        else
            throw new SyntaxException( "an unary operand", currentToken );

        return token;
    }

    private OperandNode operand() throws LexicalException, SyntaxException, IOException {
        if( literal_tokens.contains( currentToken.getTokenType() ) ){
            return literal();
        } else if( access_tokens.contains( currentToken.getTokenType() ) ) {
            return access();
        } else
            throw new SyntaxException("an operand", currentToken);
    }

    private OperandNode literal() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        if( TokenId.kw_null == currentToken.getTokenType() ){
            match("null", TokenId.kw_null);
            return new NullNode(token);
        } else if ( TokenId.literal_integer == currentToken.getTokenType() ) {
            match("literal integer", TokenId.literal_integer);
            return new IntNode(token);
        } else if ( TokenId.literal_char == currentToken.getTokenType() ) {
            match("literal character", TokenId.literal_char);
            return new CharNode(token);
        } else if ( TokenId.literal_String == currentToken.getTokenType() ) {
            match("literal String", TokenId.literal_String);
            return new StringNode(token);
        } else if ( TokenId.kw_true == currentToken.getTokenType() ) {
            match("true", TokenId.kw_true);
            return new TrueNode(token);
        } else if ( TokenId.kw_false == currentToken.getTokenType() ){
            match("false", TokenId.kw_false);
            return new FalseNode(token);
        }else
            throw new SyntaxException("a literal", currentToken);
    }

    private AccessNode access() throws LexicalException, SyntaxException, IOException {
        if( primary_tokens.contains( currentToken.getTokenType() ) ){
            AccessNode primaryNode = primary();
            ChainedNode chainedNode = optional_chain();
            primaryNode.setChainedNode(chainedNode);
            return primaryNode;
        }else
            throw new SyntaxException("an accesss", currentToken);
    }

    private VarAccessNode switch_access() throws LexicalException, SyntaxException, IOException {
        if( primary_tokens.contains( currentToken.getTokenType() ) ){
            return switch_var();
        }else
            throw new SyntaxException("an accesss", currentToken);
    }

    private VarAccessNode switch_var() throws LexicalException, SyntaxException, IOException {
        if ( TokenId.method_var_id == currentToken.getTokenType() ){
            Token token = currentToken;
            match("method or variable identifier", currentToken.getTokenType() );
            return new VarAccessNode(token);
        } else
            throw new SyntaxException("a variable", currentToken);
    }

    private AccessNode primary() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_this == currentToken.getTokenType() ){
            return this_access();
        } else if ( TokenId.kw_new == currentToken.getTokenType() ) {
            return builder_access();
        } else if ( TokenId.class_id == currentToken.getTokenType() ) {
            return static_method_access();
        } else if ( TokenId.ps_openParenthesis == currentToken.getTokenType() ) {
            return parenthesized_expression();
        } else if ( TokenId.method_var_id == currentToken.getTokenType() ){
            Token token = currentToken;
            match("method or variable identifier", currentToken.getTokenType() );
            return primary_factorized(token);
        } else
            throw new SyntaxException("a primary", currentToken);

    }

    private AccessNode this_access() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        match("this", TokenId.kw_this);
        return new ThisAccessNode(token);
    }

    private AccessNode builder_access() throws LexicalException, SyntaxException, IOException {
        match("new", TokenId.kw_new);
        Token token = currentToken;
        last_call = currentToken;
        match("class identifier", TokenId.class_id);
        match("(", TokenId.ps_openParenthesis);
        match(")", TokenId.ps_closeParenthesis);
        return new BuilderAccessNode(token);
    }

    private AccessNode static_method_access() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        match("class identifier", TokenId.class_id);
        match(".", TokenId.ps_dot);
        Token method_token = currentToken;
        match("method or variable identifier", TokenId.method_var_id);
        List<ExpressionNode> current_arguments = current_arguments();
        return new StaticMethodAccessNode(token, method_token, current_arguments);
    }

    private ParentizedExpressionNode parenthesized_expression() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        return new ParentizedExpressionNode(expressionNode);
    }

    private AccessNode primary_factorized(Token token) throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            List<ExpressionNode> expressionNodeList = current_arguments();
            return new MethodAccessNode(expressionNodeList, token);
        }else{
            return new VarAccessNode(token);
        }
    }

    private ChainedNode optional_chain() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_dot == currentToken.getTokenType() ){
            match(".", TokenId.ps_dot);
            Token token = currentToken;
            match("method or variable identifier", TokenId.method_var_id);
            return optional_chain_method_or_variable(token);
        } else {
            return null;
        }
    }

    private ChainedNode optional_chain_method_or_variable(Token token) throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            last_call = currentToken;
            List<ExpressionNode> expressionNodeList = current_arguments();
            ChainedNode chainedNode = optional_chain();
            return new ChainedMethodAccessNode(token, expressionNodeList, chainedNode);
        } else {
            last_call = null;
            ChainedNode chainedNode = optional_chain();
            return new ChainedVarAccessNode(token, chainedNode);
        }
    }

    private List<ExpressionNode> current_arguments() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        List<ExpressionNode> current_arguments = expressions_as_arguments_optional();
        last_call = currentToken;
        match(")", TokenId.ps_closeParenthesis);
        return current_arguments;
    }

    private List<ExpressionNode> expressions_as_arguments_optional() throws LexicalException, SyntaxException, IOException {
        if( expressionStart_token.contains(currentToken.getTokenType()) ){
            return expressions_as_arguments();
        }else{
            return new ArrayList<>();
        }
    }

    private List<ExpressionNode> expressions_as_arguments() throws LexicalException, SyntaxException, IOException {
        if(expression_tokens.contains( currentToken.getTokenType() ) ){
            ExpressionNode expressionNode = expression_parsing();
            List<ExpressionNode> expressionNodeList = expressions_as_arguments_factorized();
            expressionNodeList.addFirst(expressionNode);
            return expressionNodeList;
        }else
            throw new SyntaxException("an expression list", currentToken);
    }


    private List<ExpressionNode> expressions_as_arguments_factorized() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_comma == currentToken.getTokenType() ){
            match(",", TokenId.ps_comma);
            return expressions_as_arguments();
        }else{
            return new ArrayList<>();
        }
    }

    private ExpressionNode recursive_expression(ExpressionNode left_side) throws LexicalException, SyntaxException, IOException {
        if( binaryOperator_tokens.contains( currentToken.getTokenType() ) ){
            BinaryExpressionNode binaryExpressionNode = binary_operator();
            ExpressionNode right_side = unary_expression();
            binaryExpressionNode.create_binary_expression(left_side, right_side);
            return recursive_expression(binaryExpressionNode);
        }else{
            return left_side;
        }
    }

    private BinaryExpressionNode binary_operator() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        if( TokenId.op_or == currentToken.getTokenType() ){
            match("||", TokenId.op_or);
            return new OrBinaryExpNode(token);
        } else if ( TokenId.op_and == currentToken.getTokenType() ){
            match("&&", TokenId.op_and);
            return new AndBinaryExpNode(token);
        } else if ( TokenId.op_equals == currentToken.getTokenType() ){
            match("==", TokenId.op_equals);
            return new EqualsBinaryExpNode(token);
        } else if ( TokenId.op_notEquals == currentToken.getTokenType() ) {
            match("!=", TokenId.op_notEquals);
            return new NotBinaryExpNode(token);
        } else if ( TokenId.op_lessThan == currentToken.getTokenType() ) {
            match("<", TokenId.op_lessThan);
            return new LessBinaryExpNode(token);
        } else if ( TokenId.op_lessThanEqual == currentToken.getTokenType() ) {
            match("<=", TokenId.op_lessThanEqual);
            return new LessEqualsBinaryExpNode(token);
        } else if ( TokenId.op_greaterThan == currentToken.getTokenType() ) {
            match(">", TokenId.op_greaterThan);
            return new GreaterBinaryExpNode(token);
        } else if ( TokenId.op_greaterThanEqual == currentToken.getTokenType() ) {
            match(">=", TokenId.op_greaterThanEqual);
            return new GreaterEqualsBinaryExpNode(token);
        } else if ( TokenId.op_add == currentToken.getTokenType() ){
            match("+", TokenId.op_add);
            return new AddBinaryExpNode(token);
        } else if ( TokenId.op_substract == currentToken.getTokenType() ) {
            match("-", TokenId.op_substract);
            return new SubstractBinaryExpNode(token);
        } else if ( TokenId.op_multiply == currentToken.getTokenType() ) {
            match("*", TokenId.op_multiply);
            return new MultiplyBinaryExpNode(token);
        } else if ( TokenId.op_divide == currentToken.getTokenType() ) {
            match("/", TokenId.op_divide);
            return new DivisionBinaryExpNode(token);
        } else if ( TokenId.op_mod == currentToken.getTokenType() ) {
            match("%", TokenId.op_mod);
            return new ModuleBinaryExpNode(token);
        } else
            throw new SyntaxException("a binary operator", currentToken);
    }

    private StatementNode return_statement() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        match("return", TokenId.kw_return);
        ReturnNode returnNode = new ReturnNode(token);
        ExpressionNode expressionNode = return_expression();
        returnNode.insertExpression(expressionNode);
        return returnNode;
    }

    private ExpressionNode return_expression() throws LexicalException, SyntaxException, IOException {
        if( expression_tokens.contains( currentToken.getTokenType() ) ){
            return expression_parsing();
        }else{
            return null;
        }
    }

    private IfNode if_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        Token token = currentToken;
        match("if", TokenId.kw_if);
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        StatementNode if_statement = statement();
        StatementNode else_statement = else_statement();
        IfNode ifNode = new IfNode(token, expressionNode, if_statement);
        ifNode.insertElseStatement(else_statement);
        return ifNode;
    }

    private StatementNode else_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        if( TokenId.kw_else == currentToken.getTokenType() ){
            match("else", TokenId.kw_else);
            return statement();
        }else{
            return null;
        }
    }

    private WhileNode while_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        Token token = currentToken;
        match("while", TokenId.kw_while);
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        StatementNode statementNode = statement();
        return new WhileNode(token, expressionNode, statementNode);
    }

    private SwitchNode switch_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        match("switch", TokenId.kw_switch);
        match("(", TokenId.ps_openParenthesis);
        VarAccessNode switch_var = switch_access();
        match(")", TokenId.ps_closeParenthesis);
        match("{", TokenId.ps_openBrace);
        SwitchNode switchNode = new SwitchNode(switch_var);
        List<CaseNode> cases = switch_list_statement();
        switchNode.setCases(cases);
        StatementNode default_case = default_case_optional_statement();
        switchNode.setDefaultCase(default_case);
        match("}", TokenId.ps_closeBrace);
        return switchNode;
    }

    private List<CaseNode> switch_list_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        if( switch_tokens.contains( currentToken.getTokenType() ) ) {
            return switch_list_statement_parsing();
        }else{
            return new ArrayList<>();
        }
    }

    private List<CaseNode> switch_list_statement_parsing() throws LexicalException, SyntaxException, SemanticException, IOException {
        if( switch_tokens.contains( currentToken.getTokenType() ) ) {
            List<CaseNode> cases = new ArrayList<>();

            while(currentToken.getTokenType() == TokenId.kw_case){
                CaseNode case_node = switch_list_statement_parsing_recursive();
                cases.add(case_node);
            }

            return cases;

        }else{
            return new ArrayList<>();
        }
    }

    private CaseNode switch_list_statement_parsing_recursive() throws LexicalException, SyntaxException, SemanticException, IOException {
        match("case", TokenId.kw_case);
        Token token = currentToken;
        OperandNode literal = literal();
        match(":", TokenId.ps_colon);
        CaseNode caseNode = new CaseNode(token, literal);
        StatementNode statement = case_optional_statement();
        caseNode.setStatement(statement);
        return caseNode;
    }

    private StatementNode case_optional_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(statement_tokens.contains( currentToken.getTokenType() ) ){
            return statement();
        }else {
            return null;
        }
    }

    private StatementNode default_case_optional_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        if (currentToken.getTokenType() == TokenId.kw_default){
            match("default", TokenId.kw_default);
            match(":", TokenId.ps_colon);
            return statement();
        }else
            return null;
    }

    private StatementNode assignment_statement_or_call(AccessNode accessNode) throws LexicalException, SyntaxException, IOException {
        if( assigmentType_tokens.contains( currentToken.getTokenType() ) ){
            AssignmentNode assignmentNode = assignment_type(accessNode);
            ExpressionNode expressionNode = expression_parsing();
            assignmentNode.insert_expression(expressionNode);
            return assignmentNode;
        }else{
            return new CallNode(currentToken, accessNode);
        }
    }

    private AssignmentNode assignment_type(AccessNode accessNode) throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        if( TokenId.op_assignment == currentToken.getTokenType() ){
            match("=", TokenId.op_assignment);
            return new ExpAssignmentNode(token, accessNode);
        } else if ( TokenId.op_assignmentAdition == currentToken.getTokenType() ) {
            match("+=", TokenId.op_assignmentAdition);
            return new IncAssignmentNode(token, accessNode);
        } else if ( TokenId.op_assignmentSubstraction == currentToken.getTokenType() ) {
            match("-=", TokenId.op_assignmentSubstraction);
            return new DecAssignmentNode(token, accessNode);
        }else
            throw new SyntaxException(token.getLexeme(), token);
    }

}
