package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class CaseNode extends StatementNode {

    protected Token token;
    protected OperandNode literal;
    protected StatementNode case_statement;

    public CaseNode(Token token, OperandNode literal, StatementNode case_statement){
        this.token = token;
        this.literal = literal;
        this.case_statement = case_statement;
    }

    @Override
    public void verify() throws SemanticException {

    }
}
