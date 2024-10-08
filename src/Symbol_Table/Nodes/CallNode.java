package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;

public class CallNode extends StatementNode {

    protected Token token;
    protected AccessNode accessNode;

    public CallNode(Token token, AccessNode accessNode){
        this.token = token;
        this.accessNode = accessNode;
    }

    public void verify() throws SemanticException{

    }

}
