package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class DefaultNode extends StatementNode {

    protected Token token;
    protected StatementNode statementNode;

    public DefaultNode(Token token, StatementNode statementNode){
        this.token = token;
        this.statementNode = statementNode;
    }

    @Override
    public void verify() throws SemanticException {

    }
}
