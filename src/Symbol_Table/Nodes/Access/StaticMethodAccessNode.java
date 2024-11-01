package Symbol_Table.Nodes.Access;

import Symbol_Table.Class;
import Lexical_Analyzer.Token;
import Symbol_Table.Method;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;

import java.util.ArrayList;
import java.util.List;

public class StaticMethodAccessNode extends AccessNode {

    protected Token token;
    protected Token method_token;
    protected List<ExpressionNode> current_arguments;


    public StaticMethodAccessNode(Token token, Token method_token, List<ExpressionNode> current_arguments){
        this.token = token;
        this.method_token = method_token;
        this.current_arguments = current_arguments;
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
    public Type verify() throws SemanticException {
        Class class_ = SymbolTable.getInstance().getClass(token.getLexeme());

        if (class_ == null)
            throw new SemanticException(token, "class "+token.getLexeme()+" is not declared");

        Method method = class_.getMethod(method_token.getLexeme());
        if(method == null)
            throw new SemanticException(method_token, "method "+method_token.getLexeme()+" is not declared");
        else if( SymbolTable.current_method.getStatic_method() != null
                && method.getStatic_method() == null )
            throw new SemanticException(method_token, "it cannot reference to a dynamic method from a static one");
        else if( method.getStatic_method() == null )
            throw new SemanticException(method_token, "method "+method_token.getLexeme()+" is not a static method");
        else if( method.getParameters().values().size() != current_arguments.size() )
            throw new SemanticException(method_token, "number of parameters does not match");
        else{

            List<Type> method_type_list = new ArrayList<>();
            for(ExpressionNode expressionNode : current_arguments ){
                method_type_list.add(expressionNode.verify());
            }

            if(!method.conformance(method_type_list))
                throw new SemanticException(method_token, "there is no conformance in any of the parameters");

        }

        if(chainedNode == null)
            return (Type) method.getMethod_type();
        else
            return chainedNode.verify((Type) method.getMethod_type());

    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }
}
