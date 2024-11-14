package Symbol_Table.Nodes.Expression;

import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Access.ChainedNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;

public class ParentizedExpressionNode extends AccessNode {

    protected ExpressionNode expressionNode;

    public ParentizedExpressionNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    @Override
    public Type verify() throws SemanticException{
        if(chainedNode != null)
            return chainedNode.verify(expressionNode.verify());
        else
            return expressionNode.verify();
    }

    @Override
    public boolean can_be_called() {
        if(chainedNode != null)
            return chainedNode.can_be_called();
        else
            return false;
    }

    @Override
    public boolean can_be_assigned() {
        if(chainedNode != null)
            return chainedNode.can_be_assigned();
        else
            return false;
    }

    @Override
    public void generate() {
        expressionNode.generate();

        if(chainedNode != null){
            chainedNode.set_same_side(this.is_left_side_assignable);
            chainedNode.generate();
        }
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

}
