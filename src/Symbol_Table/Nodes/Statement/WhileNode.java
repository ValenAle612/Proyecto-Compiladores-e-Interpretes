package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.BooleanType;

public class WhileNode extends StatementNode {

    protected Token token;
    protected ExpressionNode condition;
    protected StatementNode while_statement;

    public WhileNode(Token token, ExpressionNode condition, StatementNode while_statement){
        this.token = token;
        this.condition = condition;
        this.while_statement = while_statement;
    }

    @Override
    public void verify() throws SemanticException{

        if(!condition.verify().same_type(new BooleanType()))
            throw new SemanticException(token, "expression condition of while is not boolean");

        while_statement.verify();

    }

}
