package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.Method;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.MethodType;
import Symbol_Table.Types.Type;
import Symbol_Table.Types.VoidType;

import java.util.ArrayList;
import java.util.List;

public class MethodAccessNode extends AccessNode {

    protected List<ExpressionNode> current_parameters;
    protected Token token;
    protected Method method;
    protected List<ExpressionNode> inverted_parameters;

    public MethodAccessNode(List<ExpressionNode> current_parameters, Token token){
        this.current_parameters = current_parameters;
        this.token = token;
        this.inverted_parameters = new ArrayList<>();
    }

    public Type verify() throws SemanticException{
        method = SymbolTable.current_class.conformance(token.getLexeme(), current_parameters);

        if( method == null )
            throw new SemanticException(token, "the method "+token.getLexeme()+" is not declared ");

        if( SymbolTable.current_method.getStatic_method() != null && method.getStatic_method() == null )
            throw new SemanticException(token, "cannot reference a dynamic method from static method");

        MethodType type = method.getMethod_type();

        if(inverted_parameters.size() == 0)
            this.set_inverted_parameters();

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

    private void set_inverted_parameters(){
        int j = current_parameters.size() - 1;

        for(int i = 0; i < current_parameters.size(); i++){
            inverted_parameters.add(current_parameters.get(j));
            j--;
        }
    }

    @Override
    public void generate() {
        if(method.is_dynamic()){
            SymbolTable.generate("LOAD 3");
            if(!method.getMethod_type().same_type(new VoidType())){
                SymbolTable.generate("RMEM 1");
                SymbolTable.generate("SWAP");
            }

            for(ExpressionNode expressionNode : inverted_parameters){
                expressionNode.generate();
                SymbolTable.generate("SWAP");
            }

            SymbolTable.generate("DUP");
            SymbolTable.generate("LOADREF 0 ; Load VT");
            SymbolTable.generate("LOADREF "+ method.getOffset());
            SymbolTable.generate("CALL");

        }else{// if is static
            if(!method.getMethod_type().same_type(new VoidType()))
                SymbolTable.generate("RMEM 1");

            for(ExpressionNode expressionNode : inverted_parameters)
                expressionNode.generate();

            SymbolTable.generate("PUSH "+method.method_label());
            SymbolTable.generate("CALL");

            if(chainedNode != null){
                chainedNode.set_same_side(is_left_side_assignable);
                chainedNode.generate();
            }

        }

    }

}
