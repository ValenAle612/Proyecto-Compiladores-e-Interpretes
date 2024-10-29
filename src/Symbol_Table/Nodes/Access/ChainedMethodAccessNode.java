package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.Class;
import Symbol_Table.Method;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;

import java.util.List;

public class ChainedMethodAccessNode extends ChainedNode {

    protected Token token;
    protected List<ExpressionNode> expressionNodeList;
    protected ChainedNode chainedNode;

    public ChainedMethodAccessNode(Token token, List<ExpressionNode> expressionNodeList, ChainedNode chainedNode){
        this.token = token;
        this.expressionNodeList = expressionNodeList;
        this.chainedNode = chainedNode;
    }

    @Override
    public Type verify(Type type) throws SemanticException {
        Type method_type;
        Class class_ = SymbolTable.getInstance().getClass(type.getCurrentType());
        if( class_ != null ){

            Method method = class_.conformance(token.getLexeme(), expressionNodeList);
            if(method != null)
                method_type = (Type) method.getMethod_type();
            else
                throw new SemanticException(token, "undeclared method in class "+class_.getToken().getLexeme());

        }else
            throw new SemanticException(token, "the access to the left side is not a valid class or is undefined");

        if(chainedNode == null)
            return method_type;
        else
            return chainedNode.verify(method_type);
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
