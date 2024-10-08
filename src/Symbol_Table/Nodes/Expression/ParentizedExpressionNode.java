package Symbol_Table.Nodes.Expression;

import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

public class ParentizedExpressionNode extends AccessNode {

    protected ExpressionNode expressionNode;

    public ParentizedExpressionNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    public Type verify() throws SemanticException{
        return null;
    }

}
