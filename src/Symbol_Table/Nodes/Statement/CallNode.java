package Symbol_Table.Nodes.Statement;

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

    @Override
    public void verify() throws SemanticException{
        accessNode.verify();

        if(!accessNode.can_be_called())
            throw new SemanticException(token, "call was expected");
    }

}
