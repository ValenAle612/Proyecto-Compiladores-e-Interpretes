package Symbol_Table.Nodes.Access;

import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.Types.Type;

public abstract class AccessNode extends OperandNode {

    protected ChainedNode chainedNode;
    protected boolean is_left_side_assignable;
    protected Type type;

    public abstract boolean can_be_called();

    public abstract boolean can_be_assigned();

    public abstract void setChainedNode(ChainedNode chainedNode);

    public abstract void generate();

    public void set_as_left_side(){
        is_left_side_assignable = true;
    }

    public abstract Type getType();

    public abstract void setType(Type type);

}
