package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.ConcreteClass;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.ClassType;
import Symbol_Table.Types.Type;

public class BuilderAccessNode extends AccessNode{

    protected Token token;
    protected ConcreteClass concrete_class;

    public BuilderAccessNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException {
        concrete_class = SymbolTable.getInstance().getClass(token.getLexeme());

        if(concrete_class == null)
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

    @Override
    public void generate() {
        SymbolTable.generate("RMEM 1");
        int amount_to_psuh = concrete_class.getAttributes().size() + 1;
        SymbolTable.generate("PUSH " + amount_to_psuh);
        SymbolTable.generate("PUSH simple_malloc");
        SymbolTable.generate("CALL");
        SymbolTable.generate("DUP");
        SymbolTable.generate("PUSH VT_"+concrete_class.getToken().getLexeme());
        SymbolTable.generate("STOREREF 0");
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Token getToken() {
        return token;
    }
}
