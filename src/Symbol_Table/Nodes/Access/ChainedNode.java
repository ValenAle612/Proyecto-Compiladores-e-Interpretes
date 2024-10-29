package Symbol_Table.Nodes.Access;

import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public abstract class ChainedNode {

    protected ChainedNode chainedNode;

    public void insertChainedNode(ChainedNode chainedNode){
        this.chainedNode = chainedNode;
    }

    public abstract Type verify(Type type) throws SemanticException;

    public abstract boolean can_be_called();

    public abstract boolean can_be_assigned();

}
