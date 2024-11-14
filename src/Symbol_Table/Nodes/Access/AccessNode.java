package Symbol_Table.Nodes.Access;

import Symbol_Table.Nodes.Expression.OperandNode;

public abstract class AccessNode extends OperandNode {

    protected ChainedNode chainedNode;
    protected boolean is_left_side_assignable;

    public abstract boolean can_be_called();

    public abstract boolean can_be_assigned();

    public abstract void setChainedNode(ChainedNode chainedNode);

    public abstract void generate();

    public void set_as_left_side(){
        is_left_side_assignable = true;
    }

}
