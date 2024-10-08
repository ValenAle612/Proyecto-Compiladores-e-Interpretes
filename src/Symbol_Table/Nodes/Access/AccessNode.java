package Symbol_Table.Nodes.Access;

import Symbol_Table.Nodes.Expression.OperandNode;

public abstract class AccessNode extends OperandNode {

    protected ChainedNode chainedNode;

    public void setChainedNode(ChainedNode chainedNode){
        this.chainedNode = chainedNode;
    }

}
