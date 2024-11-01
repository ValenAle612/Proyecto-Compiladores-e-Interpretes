package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class CaseNode{

    private Token token;
    private OperandNode literal;
    private StatementNode statement;
    Type switch_condition_type;

    public CaseNode(Token token, OperandNode literal, Type switch_condition_type) {
        this.token = token;
        this.literal = literal;
        this.switch_condition_type = switch_condition_type;
    }

    public void setStatement(StatementNode statement){
        this.statement = statement;
    }

    public void verify() throws SemanticException {

        if (switch_condition_type!= null && !literal.verify().same_type(switch_condition_type)) {
            throw new SemanticException(token, "Case type doesn't match switch expression type");
        }

        if(statement != null)
            statement.verify();
    }

}

