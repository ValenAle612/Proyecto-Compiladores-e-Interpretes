package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import Symbol_Table.Types.VoidType;

public class CallNode extends StatementNode {

    protected Token token;
    protected AccessNode accessNode;
    protected Type type;

    public CallNode(Token token, AccessNode accessNode){
        this.token = token;
        this.accessNode = accessNode;
    }

    @Override
    public void verify() throws SemanticException{
        type = accessNode.verify();

        if(!accessNode.can_be_called())
            throw new SemanticException(token, "call was expected");
    }

    @Override
    public void generate() {
        accessNode.generate();
        if(!type.same_type(new VoidType()))
            SymbolTable.generate("POP ; value is will not be assigned, is discarded");
    }

}
