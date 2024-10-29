package Symbol_Table.Nodes.Access;

import Symbol_Table.Nodes.Expression.OperandNode;

public abstract class AccessNode extends OperandNode {

    protected ChainedNode chainedNode;

    public abstract boolean can_be_called();

    public abstract boolean can_be_assigned();

    public abstract void setChainedNode(ChainedNode chainedNode);

}
