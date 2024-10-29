package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.Method;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.MethodType;
import Symbol_Table.Types.Type;

import java.util.List;

public class MethodAccessNode extends AccessNode {

    protected List<ExpressionNode> current_parameters;
    protected Token token;

    public MethodAccessNode(List<ExpressionNode> current_parameters, Token token){
        this.current_parameters = current_parameters;
        this.token = token;
    }

    public Type verify() throws SemanticException{
        Method method = SymbolTable.current_class.conformance(token.getLexeme(), current_parameters);

        if( method == null )
            throw new SemanticException(token, "the method "+token.getLexeme()+" is not declared ");

        if( SymbolTable.current_method.getStatic_method() != null && method.getStatic_method() == null )
            throw new SemanticException(token, "cannot reference a dynamic method from static method");

        MethodType type = method.getMethod_type();

        if(chainedNode == null)
            return (Type) type;
        else
            return chainedNode.verify((Type) type);

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
