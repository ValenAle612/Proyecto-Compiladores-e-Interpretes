package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.BooleanType;

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

    @Override
    public void verify() throws SemanticException{
        if(condition.verify().same_type(new BooleanType())){
            if_statement.verify();
            if(else_statement != null)
                else_statement.verify();
        }else
            throw new SemanticException(token, "boolean expression was expected");
    }

}
