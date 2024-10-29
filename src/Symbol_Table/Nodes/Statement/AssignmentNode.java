package Symbol_Table.Nodes.Statement;

import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;

public abstract class AssignmentNode extends StatementNode {

    protected AccessNode left_node;
    protected ExpressionNode expressionNode;

    public abstract void insert_expression(ExpressionNode expressionNode);

}
