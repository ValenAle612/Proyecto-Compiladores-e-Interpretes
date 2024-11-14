package Symbol_Table.Nodes.Access;

import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public abstract class ChainedNode {

    protected ChainedNode chainedNode;

    protected boolean is_left_side_assignable = false;

    public void insertChainedNode(ChainedNode chainedNode){
        this.chainedNode = chainedNode;
    }

    public abstract Type verify(Type type) throws SemanticException;

    public abstract boolean can_be_called();

    public abstract boolean can_be_assigned();

    public abstract void generate();

    public void set_same_side(boolean left_side){
        this.is_left_side_assignable = left_side;
    }

}
