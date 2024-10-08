package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class WhileNode extends StatementNode {

    protected Token token;
    protected ExpressionNode condition;
    protected StatementNode while_statement;

    public WhileNode(Token token, ExpressionNode condition, StatementNode while_statement){
        this.token = token;
        this.condition = condition;
        this.while_statement = while_statement;
    }

    public void verify() throws SemanticException{

    }

}
