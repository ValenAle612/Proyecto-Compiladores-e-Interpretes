package Symbol_Table.Nodes.Access;

import Symbol_Table.Class;
import Lexical_Analyzer.Token;
import Symbol_Table.Method;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import Symbol_Table.Types.VoidType;

import java.util.ArrayList;
import java.util.List;

public class StaticMethodAccessNode extends AccessNode {

    protected Token token;
    protected Token method_token;
    protected List<ExpressionNode> current_arguments;
    protected Method method;
    protected List<ExpressionNode> inverted_parameters;


    public StaticMethodAccessNode(Token token, Token method_token, List<ExpressionNode> current_arguments){
        this.token = token;
        this.method_token = method_token;
        this.current_arguments = current_arguments;
        this.inverted_parameters = new ArrayList<>();
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

        this.method = class_.getMethod(method_token.getLexeme());

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

        if(inverted_parameters.size() == 0)
            this.set_inverted_parameters();

        if(chainedNode == null)
            return (Type) method.getMethod_type();
        else
            return chainedNode.verify((Type) method.getMethod_type());

    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public void generate() {

        if(method.getStatic_method() != null){

            if(!method.getMethod_type().same_type(new VoidType()))
                SymbolTable.generate("RMEM 1");

            for(ExpressionNode expressionNode : inverted_parameters)
                expressionNode.generate();

            SymbolTable.generate("PUSH "+method.method_label());
            SymbolTable.generate("CALL");

        }

        if(chainedNode != null){
            chainedNode.set_same_side(this.is_left_side_assignable);
            chainedNode.generate();
        }

    }

    private void set_inverted_parameters(){
        int j = current_arguments.size() - 1;

        for(int i = 0; i < current_arguments.size(); i++){
            inverted_parameters.add(current_arguments.get(j));
            j--;
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
