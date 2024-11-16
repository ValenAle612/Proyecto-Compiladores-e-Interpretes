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

    @Override
    public void generate() {
        SymbolTable.generate("LOAD 3");
        if(chainedNode !=null){
            chainedNode.set_same_side(this.is_left_side_assignable);
            chainedNode.generate();
        }
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

}