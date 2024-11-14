package Symbol_Table.Nodes.Expression;

import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public abstract class ExpressionNode {

    public abstract Type verify() throws SemanticException;

    public abstract void generate();

}
