package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class LocalVarNode extends StatementNode {

    protected Token token;
    protected ExpressionNode expressionNode;

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

    public void verify() throws SemanticException{

    }

}
