package Symbol_Table.Nodes.Statement;

import Symbol_Table.SemanticException;

public abstract class StatementNode {

    public abstract void verify() throws SemanticException;

    public abstract void generate();

}
