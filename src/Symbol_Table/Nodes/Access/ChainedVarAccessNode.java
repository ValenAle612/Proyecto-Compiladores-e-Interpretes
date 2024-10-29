package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.Attribute;
import Symbol_Table.ConcreteClass;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.ConcreteType;
import Symbol_Table.Types.Type;

public class ChainedVarAccessNode extends ChainedNode {

    protected Token token;
    protected ChainedNode chainedNode;

    public ChainedVarAccessNode(Token token, ChainedNode chainedNode){
        this.token = token;
        this.chainedNode = chainedNode;
    }

    @Override
    public Type verify(Type type) throws SemanticException {
        ConcreteType concreteType;
        ConcreteClass class_ = SymbolTable.getInstance().getClass(type.getCurrentType());
        if(class_ != null){

            Attribute attribute = class_.getAttribute(token.getLexeme());
            if(attribute != null){
                if( attribute.getVisibility() == TokenId.kw_private
                        && !SymbolTable.current_class.getToken().getLexeme().equals(attribute.getClass_that_contains_the_attribute().getLexeme()) )
                    throw new SemanticException(token, "is not possible access to a private attribute, token: "+token.getLexeme());
                else
                    concreteType = attribute.getAttribute_type();
            } else
                throw new SemanticException(token, "attribute "+token.getLexeme()+" is not declared or it cannot be accessed from the current class");

        }else
            throw new SemanticException(token, "the access type to the left is not declared or is not a valid class");

        if(chainedNode != null){
            return chainedNode.verify(concreteType);
        }else
            return concreteType;

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

}
