package Symbol_Table.Nodes.Access;

import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public abstract class ChainedNode {

    protected ChainedNode chainedNode;

    public void insertChainedNode(ChainedNode chainedNode){
        this.chainedNode = chainedNode;
    }

    public abstract Type verify() throws SemanticException;

}
