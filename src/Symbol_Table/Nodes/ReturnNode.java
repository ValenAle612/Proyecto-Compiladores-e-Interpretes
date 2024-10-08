package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class ReturnNode extends StatementNode {

    protected Token token;
    protected ExpressionNode expressionNode;

    public ReturnNode(Token token){
        this.token = token;
    }

    public void insertExpression(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    public void verify() throws SemanticException{

    }

}
