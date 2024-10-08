package Symbol_Table.Nodes;

import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Statement.StatementNode;

public abstract class AssignmentNode extends StatementNode {

    protected AccessNode left_node;

}
