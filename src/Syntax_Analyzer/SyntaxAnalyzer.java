package Syntax_Analyzer;

import Lexical_Analyzer.LexicalAnalyzer;
import Lexical_Analyzer.LexicalException;
import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.*;
import Symbol_Table.Nodes.*;
import Symbol_Table.Nodes.Access.*;
import Symbol_Table.Nodes.Expression.*;
import Symbol_Table.Nodes.Expression.BE.*;
import Symbol_Table.Nodes.Literal.*;
import Symbol_Table.Nodes.Statement.EmptyStatementNode;
import Symbol_Table.Nodes.Statement.StatementNode;

import java.io.IOException;
import java.util.*;

public class SyntaxAnalyzer {

    LexicalAnalyzer lexicalAnalyzer;
    Token currentToken;
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
    private final Set<TokenId> localVariable_tokens = new HashSet<>(Set.of(TokenId.class_id, TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int));
    private final Set<TokenId> switch_tokens = new HashSet<>(Set.of(TokenId.kw_case, TokenId.kw_default) );

    private final Set<TokenId> assigmentType_tokens = new HashSet<>(Set.of(TokenId.op_assignment, TokenId.op_assignmentAdition,
                                                                            TokenId.op_assignmentSubstraction));
    private final Set<TokenId> attribute_tokens = new HashSet<>(Set.of(TokenId.kw_public, TokenId.kw_private));
    private final Set<TokenId> method_tokens = new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id,
                                                                    TokenId.kw_boolean, TokenId.kw_int, TokenId.kw_char));
    private final Set<TokenId> method_header_tokens = new HashSet<>(Set.of(TokenId.kw_static, TokenId.kw_void, TokenId.class_id,
                                                                            TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int));

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
            concrete_class();
        }else
            throw new SyntaxException("class", currentToken);
    }

    private void concrete_class() throws LexicalException, SyntaxException, SemanticException, IOException {
        match("class", TokenId.kw_class);
        Token token = currentToken;
        match("class identifier", TokenId.class_id);
        ConcreteClass concreteClass = new ConcreteClass(token);
        Token inherits_class = inherits_from();
        concreteClass.setInherit_class_token(inherits_class);
        SymbolTable.getInstance().save_class(concreteClass, token.getLexeme());
        match("{", TokenId.ps_openBrace);
        members_parsing();
        match("}", TokenId.ps_closeBrace);
    }

    private Token inherits_from() throws LexicalException, SyntaxException, IOException {

        Token token = null;

        if(TokenId.kw_extends == currentToken.getTokenType() ){
            match("extends", TokenId.kw_extends);
            token = currentToken;
            match("class identifier", TokenId.class_id);

            return token;
        }else{
            //ε
        }

        return new Token(TokenId.class_id, "Object", 0);
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
        if(attribute_tokens.contains(currentToken.getTokenType()))
            attribute();
        else if(method_tokens.contains(currentToken.getTokenType()))
            method();
        else
            throw new SyntaxException("public|private|static", currentToken);
    }

    private void method() throws LexicalException, SyntaxException, SemanticException, IOException {
        method_header();
        block();
    }

    private void method_header() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(method_header_tokens.contains(currentToken.getTokenType())){
            TokenId is_static = static_optional();
            MethodType methodType = method_type();
            Token token = currentToken;
            match("method or variable identifier", TokenId.method_var_id);
            Method method = new Method(new HashMap<String, Parameter>(), new ArrayList<Parameter>(), token, is_static, methodType);
            SymbolTable.current_method = method;
            formal_arguments();
            SymbolTable.current_class.save_method(method);
        }else
            throw new SyntaxException("a method header", currentToken);
    }

    private TokenId static_optional() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.kw_static == currentToken.getTokenType()){
            match("static", TokenId.kw_static);
            return TokenId.kw_static;
        }else{
            //ε
        }
        return null;
    }

    private MethodType method_type() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(type_tokens.contains(currentToken.getTokenType())){
            return type();
        }else if(TokenId.kw_void == currentToken.getTokenType()){
            match("void", TokenId.kw_void);
            return new VoidType();
        }else
            throw new SyntaxException("a method type", currentToken);
    }

    private void attribute() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.kw_public == currentToken.getTokenType() || TokenId.kw_private == currentToken.getTokenType()) {
            TokenId tokenId = visibility();
            Type type = type();
            attributes_list(tokenId, type);
            match(";", TokenId.ps_semicolon);
        }else
            throw new SyntaxException("an attribute", currentToken);
    }

    private void attributes_list(TokenId tokenId, Type type) throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.method_var_id == currentToken.getTokenType()){
            Token token = currentToken;
            match("method or variable identifier", currentToken.getTokenType());
            Attribute attribute = new Attribute(token, tokenId, type);
            SymbolTable.current_class.save_attribute(attribute);
            SymbolTable.current_attribute = attribute;
            attributes_list_factorized(tokenId, type);
        }else
            throw new SyntaxException("a method or variable identifier", currentToken);
    }

    private void attributes_list_factorized(TokenId tokenId, Type type) throws LexicalException, SyntaxException, SemanticException, IOException {
        if(TokenId.ps_comma == currentToken.getTokenType()){
            match(",", TokenId.ps_comma);
            attributes_list(tokenId, type);
        }else{
            //ε
        }
    }

    private TokenId visibility() throws LexicalException, SyntaxException, IOException {
        if( TokenId.kw_public == currentToken.getTokenType() ){
            match("public", TokenId.kw_public);
            return TokenId.kw_public;
        }else if ( TokenId.kw_private == currentToken.getTokenType() ){
            match("private", TokenId.kw_private);
            return TokenId.kw_private;
        }else{
            throw new SyntaxException("public | private", currentToken);
        }
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
            throw new SyntaxException("void | int | char | boolean", currentToken);
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
        ConcreteType type = type();
        Token token = currentToken;
        match("method or variable identifier",TokenId.method_var_id);
        Parameter parameter = new Parameter(token,type);
        SymbolTable.current_method.save_parameter(token.getLexeme(), parameter);
    }

    private void formal_arguments_factorized() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( TokenId.ps_comma == currentToken.getTokenType() ){
            match(",", TokenId.ps_comma);
            formal_arguments_parsing();
        }else{
            //ε
        }
    }

    private BlockNode block() throws LexicalException, SyntaxException, IOException, SemanticException {
        match("{", TokenId.ps_openBrace);
        BlockNode blockNode = new BlockNode();
        statement_parsing(blockNode);
        match("}",TokenId.ps_closeBrace);
        return blockNode;
    }

    private void statement_parsing(BlockNode blockNode) throws LexicalException, SyntaxException, IOException, SemanticException {
        if( statement_tokens.contains( currentToken.getTokenType() ) ){
            StatementNode statementNode = statement();
            blockNode.insert_statement(statementNode);
            statement_parsing(blockNode);
        }
    }

    private StatementNode statement() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( TokenId.ps_semicolon == currentToken.getTokenType() ){// :
            match(";", TokenId.ps_semicolon);
            return new EmptyStatementNode();
        } else if ( TokenId.kw_var == currentToken.getTokenType() ){// var
            StatementNode varNode = local_variable();
            match(";",TokenId.ps_semicolon);
            return varNode;
        } else if ( TokenId.kw_return == currentToken.getTokenType() ){
            StatementNode returnNode = return_statement();
            match(";",TokenId.ps_semicolon);
            return returnNode;
        } else if ( TokenId.kw_if == currentToken.getTokenType() ){
            IfNode ifNode = if_statement();
            return ifNode;
        } else if ( TokenId.kw_while == currentToken.getTokenType() ){
            WhileNode whileNode = while_statement();
            return whileNode;
        } else if ( TokenId.kw_switch == currentToken.getTokenType() ){
            SwitchNode switchNode = switch_statement();
            return switchNode;
        }else  if ( access_tokens.contains( currentToken.getTokenType() ) ) {
            AccessNode accessNode = access();
            StatementNode statementNode = assignment_statement_or_call(accessNode);
            match(";",TokenId.ps_semicolon);
            return statementNode;
        }else if ( TokenId.ps_openBrace == currentToken.getTokenType() ) {
            BlockNode blockNode = block();
            return blockNode;
        } else
            throw new SyntaxException("a statement", currentToken);

    }

    private LocalVarNode local_variable() throws LexicalException, SyntaxException, IOException {
        match("var", TokenId.kw_var);
        Token token = currentToken;
        LocalVarNode localVarNode = new LocalVarNode(token);
        match("method or variable identifier",TokenId.method_var_id);
        match("=",TokenId.op_assignment);
        localVarNode.setExpressionNode(expression_parsing());
        return localVarNode;
    }

    private ExpressionNode expression_parsing() throws LexicalException, SyntaxException, IOException {
        if( unaryExpressions_tokens.contains( currentToken.getTokenType() ) ){
            ExpressionNode left_node = unary_expression();
            return recursive_expression(left_node);
        }else
            throw new SyntaxException("an expression", currentToken);
    }

    private ExpressionNode unary_expression() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_add == currentToken.getTokenType() || TokenId.op_substract == currentToken.getTokenType() ||
                TokenId.op_not == currentToken.getTokenType() ){
            Token token = unary_operand();
            OperandNode operandNode = operand();
            return new UnaryExpressionNode(token,operandNode);
        }else if( operand_tokens.contains( currentToken.getTokenType() ) ){
            return operand();
        }else
            throw new SyntaxException("an unary expression", currentToken);
    }

    private Token unary_operand() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_add == currentToken.getTokenType() ){
            Token token = currentToken;
            match( "+", TokenId.op_add );
            return token;
        }else if( TokenId.op_substract == currentToken.getTokenType() ){
            Token token = currentToken;
            match( "-", TokenId.op_substract );
            return token;
        }else if( TokenId.op_not == currentToken.getTokenType() ){
            Token token = currentToken;
            match( "!", TokenId.op_not );
            return token;
        }
        else
            throw new SyntaxException( "an unary operand", currentToken );
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
        if( TokenId.kw_null == currentToken.getTokenType() ){
            Token token = currentToken;
            match("null", TokenId.kw_null);
            return new NullNode(token);
        } else if ( TokenId.literal_integer == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("literal integer", TokenId.literal_integer);
            return new IntNode(token);
        } else if ( TokenId.literal_char == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("literal character", TokenId.literal_char);
            return new CharNode(token);
        } else if ( TokenId.literal_String == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("literal String", TokenId.literal_String);
            return new StringNode(token);
        } else if ( TokenId.kw_true == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("true", TokenId.kw_true);
            return new TrueNode(token);
        } else if ( TokenId.kw_false == currentToken.getTokenType() ){
            Token token = currentToken;
            match("false", TokenId.kw_false);
            return new FalseNode(token);
        }else
            throw new SyntaxException("a literal", currentToken);
    }


    private AccessNode access() throws LexicalException, SyntaxException, IOException {
        if( primary_tokens.contains( currentToken.getTokenType() ) ){
            AccessNode primaryNode = primary();
            ChainedNode chainedNode = optional_chain();;
            primaryNode.setChainedNode(chainedNode);
            return primaryNode;
        }else
            throw new SyntaxException("an accesss", currentToken);
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
        match("class identifier", TokenId.class_id);
        match("(", TokenId.ps_openParenthesis);
        match(")", TokenId.ps_closeParenthesis);
        return new BuilderAccessNode(token);
    }

    private MethodAccessNode static_method_access() throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        match("class identifier", TokenId.class_id);
        match(".", TokenId.ps_dot);
        match("method or variable identifier", TokenId.method_var_id);
        List<ExpressionNode> currentArguments = current_arguments();
        MethodAccessNode methodAccessNode = new MethodAccessNode(currentArguments, token);
        return methodAccessNode;
    }

    private ParentizedExpressionNode parenthesized_expression() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        return new ParentizedExpressionNode(expressionNode);
    }

    private AccessNode primary_factorized(Token token) throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            List<ExpressionNode> currentArguments = current_arguments();
            return new MethodAccessNode(currentArguments, token);
        }else{
            return new VarAccessNode(token);
        }
    }

    private ChainedNode optional_chain() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_dot == currentToken.getTokenType() ){
            match(".", TokenId.ps_dot);
            Token token = currentToken;
            match("method or variable identifier", TokenId.method_var_id);
            ChainedNode chainedNode = optional_chain_method_or_variable(token);
            return chainedNode;
        } else
            return null;
    }

    private ChainedNode optional_chain_method_or_variable(Token token) throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_openParenthesis == currentToken.getTokenType() ){
            List<ExpressionNode> expressionNodeList = current_arguments();
            ChainedNode chainedNode = optional_chain();
            return new ChainMethodAccessNode(token, expressionNodeList, chainedNode);
        } else {
            ChainedNode chainedNode = optional_chain();
            return new ChainVarAccessNode(token, chainedNode);
        }
    }

    private List<ExpressionNode> current_arguments() throws LexicalException, SyntaxException, IOException {
        match("(", TokenId.ps_openParenthesis);
        List<ExpressionNode> currentArguments = expressions_as_arguments_optional();
        match(")", TokenId.ps_closeParenthesis);
        return currentArguments;
    }

    private List<ExpressionNode> expressions_as_arguments_optional() throws LexicalException, SyntaxException, IOException {
        if( expressionStart_token.contains( currentToken.getTokenType() ) ){
            return expressions_as_arguments();
        }else{
            return new ArrayList<ExpressionNode>();
        }
    }

    private List<ExpressionNode> expressions_as_arguments() throws LexicalException, SyntaxException, IOException {
        if(expression_tokens.contains( currentToken.getTokenType() ) ){
            ExpressionNode expressionNode = expression_parsing();
            List<ExpressionNode> expressionNodeList = expressions_as_arguments_factorized();
            expressionNodeList.add(expressionNode);
            return expressionNodeList;
        }else
            throw new SyntaxException("an expression list", currentToken);
    }


    private List<ExpressionNode> expressions_as_arguments_factorized() throws LexicalException, SyntaxException, IOException {
        if( TokenId.ps_comma == currentToken.getTokenType() ){
            match(",", TokenId.ps_comma);
            return expressions_as_arguments();
        }else{
            return new ArrayList<ExpressionNode>();
        }
    }

    private ExpressionNode recursive_expression(ExpressionNode left_node) throws LexicalException, SyntaxException, IOException {
        if( binaryOperator_tokens.contains( currentToken.getTokenType() ) ){
            BinaryExpressionNode binaryExpressionNode = binary_operator();
            ExpressionNode right_node = unary_expression();
            binaryExpressionNode.create_binary_expression(left_node, right_node);
            ExpressionNode node = recursive_expression(binaryExpressionNode);
            return node;
        }else{
            return left_node;
        }
    }

    private BinaryExpressionNode binary_operator() throws LexicalException, SyntaxException, IOException {
        if( TokenId.op_or == currentToken.getTokenType() ){
            Token token = currentToken;
            match("||", TokenId.op_or);
            return new OrBENode(token);
        } else if ( TokenId.op_and == currentToken.getTokenType() ){
            Token token = currentToken;
            match("&&", TokenId.op_and);
            return new AndBENode(token);
        } else if ( TokenId.op_equals == currentToken.getTokenType() ){
            Token token = currentToken;
            match("==", TokenId.op_equals);
            return new EqualsBENode(token);
        } else if ( TokenId.op_notEquals == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("!=", TokenId.op_notEquals);
            return new NotBENode(token);
        } else if ( TokenId.op_lessThan == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("<", TokenId.op_lessThan);
            return new LessBENode(token);
        } else if ( TokenId.op_lessThanEqual == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("<=", TokenId.op_lessThanEqual);
            return new LessEqualsBENode(token);
        } else if ( TokenId.op_greaterThan == currentToken.getTokenType() ) {
            Token token = currentToken;
            match(">", TokenId.op_greaterThan);
            return new GreaterBENode(token);
        } else if ( TokenId.op_greaterThanEqual == currentToken.getTokenType() ) {
            Token token = currentToken;
            match(">=", TokenId.op_greaterThanEqual);
            return new GreaterEqualsBENode(token);
        } else if ( TokenId.op_add == currentToken.getTokenType() ){
            Token token = currentToken;
            match("+", TokenId.op_add);
            return new AddBENode(token);
        } else if ( TokenId.op_substract == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("-", TokenId.op_substract);
            return new SubstractBENode(token);
        } else if ( TokenId.op_multiply == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("*", TokenId.op_multiply);
            return new MultiplyBENode(token);
        } else if ( TokenId.op_divide == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("/", TokenId.op_divide);
            return new DivisionBENode(token);
        } else if ( TokenId.op_mod == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("%", TokenId.op_mod);
            return new ModuleBENode(token);
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

    private IfNode if_statement() throws LexicalException, SyntaxException, IOException, SemanticException {
        Token token = currentToken;
        match("if", TokenId.kw_if);
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        StatementNode if_statement = statement();
        StatementNode else_statement = else_statement();
        IfNode ifNode = new IfNode(token,expressionNode,if_statement);
        ifNode.insertElseStatement(else_statement);
        return ifNode;
    }

    private StatementNode else_statement() throws LexicalException, SyntaxException, IOException, SemanticException {
        if( TokenId.kw_else == currentToken.getTokenType() ){
            match("else", TokenId.kw_else);
            return statement();
        }else{
            return null;
        }
    }

    private WhileNode while_statement() throws LexicalException, SyntaxException, IOException, SemanticException {
        Token token = currentToken;
        match("while", TokenId.kw_while);
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        StatementNode statementNode = statement();
        WhileNode whileNode = new WhileNode(token, expressionNode, statementNode);
        return whileNode;
    }

    private SwitchNode switch_statement() throws LexicalException, SyntaxException, IOException, SemanticException {
        Token token = currentToken;
        match("switch", TokenId.kw_switch);
        match("(", TokenId.ps_openParenthesis);
        ExpressionNode expressionNode = expression_parsing();
        match(")", TokenId.ps_closeParenthesis);
        match("{", TokenId.ps_openBrace);
        SwitchNode switchNode = new SwitchNode(token,expressionNode);
        SwitchNode switchNodeWithCasesAndDefault = switch_list_statement(switchNode);
        match("}", TokenId.ps_closeBrace);
        return switchNodeWithCasesAndDefault;
    }

    private SwitchNode switch_list_statement(SwitchNode switchNode) throws LexicalException, SyntaxException, IOException, SemanticException {
        if( switch_tokens.contains( currentToken.getTokenType() ) ) {
            return switch_list_statement_parsing(switchNode);
        }else{
            //ε
        }
        return switchNode;
     }

    private SwitchNode switch_list_statement_parsing(SwitchNode switchNode) throws LexicalException, SyntaxException, IOException, SemanticException {
        if( TokenId.kw_case == currentToken.getTokenType() ){
            Token token = currentToken;
            match("case", TokenId.kw_case);
            OperandNode operandNode = literal();
            match(":", TokenId.ps_colon);
            StatementNode statementNode = case_optional_statement();
            CaseNode caseNode = new CaseNode(token, operandNode, statementNode);
            switchNode.insertCaseBlock(caseNode);
            return switch_list_statement_parsing(switchNode);
        }else if( TokenId.kw_default == currentToken.getTokenType() ) {
            Token token = currentToken;
            match("default", TokenId.kw_default);
            match(":", TokenId.ps_colon);
            StatementNode statementNode = statement();
            DefaultNode defaultNode = new DefaultNode(token, statementNode);
            switchNode.setDefaultNode(defaultNode);
            return switch_list_statement_parsing(switchNode);
        }

        return switchNode;
    }

    private StatementNode case_optional_statement() throws LexicalException, SyntaxException, SemanticException, IOException {
        if(statement_tokens.contains( currentToken.getTokenType() ) ){
            return statement();
        }else{
            return null;
        }
    }

    private StatementNode assignment_statement_or_call(AccessNode accessNode) throws LexicalException, SyntaxException, IOException {
        if( assigmentType_tokens.contains( currentToken.getTokenType() ) ){
            AssignmentNode assignmentNode = assignment_type(accessNode);
            ExpressionNode expressionNode = expression_parsing();
            return assignmentNode;
        }else{
            return new CallNode(currentToken, accessNode);
        }
    }

    private AssignmentNode assignment_type(AccessNode accessNode) throws LexicalException, SyntaxException, IOException {
        Token token = currentToken;
        if( TokenId.op_assignment == currentToken.getTokenType() ){
            match("=", TokenId.op_assignment);
            ExpressionNode expressionNode = expression_parsing();
            return new ExpAssignmentNode(token, accessNode, expressionNode);
        } else if ( TokenId.op_assignmentAdition == currentToken.getTokenType() ) {
            match("+=", TokenId.op_assignmentAdition);
            return new DecAssignmentNode(token, accessNode);
        } else if ( TokenId.op_assignmentSubstraction == currentToken.getTokenType() ) {
            match("-=", TokenId.op_assignmentSubstraction);
            return new IncAssignmentNode(token, accessNode);
        } else
            throw new SyntaxException(token.getLexeme(), token);
    }

}
