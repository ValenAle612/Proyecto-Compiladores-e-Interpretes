package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.ClassType;
import Symbol_Table.Types.Type;

public class ThisAccessNode extends AccessNode{

    protected Token token;

    public ThisAccessNode(Token token){
        this.token = token;
    }

    @Override
    public Type verify() throws SemanticException{

        if(SymbolTable.current_method.getStatic_method() == null){
            Type type = new ClassType(SymbolTable.current_class.getToken());
            if(chainedNode != null)
                return chainedNode.verify(type);
            else
                return type;
        }else
            throw new SemanticException(token, "cannot exists a this in a static method");

    }

    @Override
    public boolean can_be_called() {
        if( chainedNode != null )
            return chainedNode.can_be_called();
        else
            return true;
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