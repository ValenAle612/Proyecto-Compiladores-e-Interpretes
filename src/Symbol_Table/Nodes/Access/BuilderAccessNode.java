package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.ConcreteClass;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.ClassType;
import Symbol_Table.Types.Type;

public class BuilderAccessNode extends AccessNode{

    protected Token token;

    public BuilderAccessNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException {
        ConcreteClass concreteClass = SymbolTable.getInstance().getClass(token.getLexeme());
        if(concreteClass == null)
            throw new SemanticException(token, "trying to create a constructor of a non-existent class");

        Type type = new ClassType(token);

        if(chainedNode == null)
            return type;
        else
            return chainedNode.verify( type );
    }

    @Override
    public boolean can_be_called() {
        if( chainedNode != null )
            return chainedNode.can_be_called();
        else return true;
    }

    @Override
    public boolean can_be_assigned() {
        if( chainedNode != null )
            return chainedNode.can_be_assigned();
        else
            return false;
    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }
}
