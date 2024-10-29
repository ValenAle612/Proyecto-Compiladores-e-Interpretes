package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;

public class LocalVarNode extends StatementNode {

    protected Token token;
    protected ExpressionNode expressionNode;
    protected Type type;

    public LocalVarNode(Token token){
        this.token = token;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public Token getToken(){
        return token;
    }

    public void setExpressionNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode(){
        return expressionNode;
    }

    @Override
    public void verify() throws SemanticException{
        if(this.expressionNode != null)
            this.setType(expressionNode.verify());

        for(BlockNode blockNode : SymbolTable.block_stack) {
            if(blockNode.getLocalVariable(token.getLexeme()) != null)
                throw new SemanticException(token, "variable already declared");
        }

        if(expressionNode.verify().getCurrentType().equals("void"))
            throw new SemanticException(token, "is not possible assign something with void type to the variable "+token.getLexeme());

        SymbolTable.getCurrent_block().insertLocalVariable(this);

        if(expressionNode.verify().getCurrentType().equals("null"))
            throw new SemanticException(token, "is not possible assign something with null type to the variable "+token.getLexeme());

    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
    }

}
