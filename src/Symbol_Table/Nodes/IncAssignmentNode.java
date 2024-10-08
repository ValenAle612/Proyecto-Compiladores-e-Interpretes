package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.SemanticException;

public class IncAssignmentNode extends AssignmentNode {

    protected Token token;
    protected AccessNode accessNode;

    public IncAssignmentNode(Token token, AccessNode accessNode){
        this.token = token;
        this.accessNode = accessNode;
    }

    public void verify() throws SemanticException{

    }

}
