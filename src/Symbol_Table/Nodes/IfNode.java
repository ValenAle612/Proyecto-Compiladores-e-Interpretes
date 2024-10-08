package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class IfNode extends StatementNode {

    protected Token token;
    protected ExpressionNode condition;
    protected StatementNode if_statement;
    protected StatementNode else_statement;

    public IfNode(Token token, ExpressionNode condition, StatementNode if_statement){
        this.token = token;
        this.condition = condition;
        this.if_statement = if_statement;
    }

    public void insertElseStatement(StatementNode else_statement){
        this.else_statement = else_statement;
    }

    public void verify() throws SemanticException{

    }

}
