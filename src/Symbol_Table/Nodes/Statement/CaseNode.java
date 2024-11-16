package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class CaseNode extends StatementNode{

    private Token token;
    private OperandNode literal;
    private Type switch_condition_type;
    private StatementNode statement;

    public CaseNode(Token token, OperandNode literal) {
        this.token = token;
        this.literal = literal;
    }

    public void setStatement(StatementNode statement){
        this.statement = statement;
    }

    public OperandNode getLiteral(){
        return literal;
    }

    public Token getToken(){
        return token;
    }

    public void setSwitch_condition_type(Type type){
        this.switch_condition_type = type;
    }

    @Override
    public void verify() throws SemanticException {

        if (switch_condition_type!= null && !literal.verify().same_type(switch_condition_type)) {
            throw new SemanticException(token, "Case type doesn't match switch expression type");
        }

        if(statement != null)
            statement.verify();
    }

    @Override
    public void generate() {
        if(statement != null)
            statement.generate();
    }

}

