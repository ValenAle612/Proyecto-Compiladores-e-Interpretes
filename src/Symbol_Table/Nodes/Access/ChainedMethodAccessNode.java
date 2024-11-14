package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Symbol_Table.Class;
import Symbol_Table.Method;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import Symbol_Table.Types.VoidType;

import java.util.ArrayList;
import java.util.List;

public class ChainedMethodAccessNode extends ChainedNode {

    protected Token token;
    protected List<ExpressionNode> expressionNodeList;
    protected ChainedNode chainedNode;

    protected Method method;
    protected Type method_type;
    protected List<ExpressionNode> inverted_parameters;

    public ChainedMethodAccessNode(Token token, List<ExpressionNode> expressionNodeList, ChainedNode chainedNode){
        this.token = token;
        this.expressionNodeList = expressionNodeList;
        this.chainedNode = chainedNode;
        this.inverted_parameters = new ArrayList<>();
    }

    @Override
    public Type verify(Type type) throws SemanticException {

        Class class_ = SymbolTable.getInstance().getClass(type.getCurrentType());
        if( class_ != null ){

            method = class_.conformance(token.getLexeme(), expressionNodeList);
            if(method != null)
                method_type = (Type) method.getMethod_type();
            else
                throw new SemanticException(token, "undeclared method in class "+class_.getToken().getLexeme());

        }else
            throw new SemanticException(token, "the access to the left side is not a valid class or is undefined");

        if(inverted_parameters.size() == 0)
            this.set_inverted_parameters();

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

    private void set_inverted_parameters(){
        int j = expressionNodeList.size() - 1;

        for(int i = 0; i < expressionNodeList.size(); i++){
            inverted_parameters.add(expressionNodeList.get(j));
            j--;
        }
    }

    @Override
    public void generate(){

        if(method.is_dynamic()){
            if(!method_type.same_type(new VoidType())){
                SymbolTable.generate("RMEM 1");
                SymbolTable.generate("SWAP");
            }

            for(ExpressionNode expressionNode : inverted_parameters){
                expressionNode.generate();
                SymbolTable.generate("SWAP");
            }

            SymbolTable.generate("DUP");
            SymbolTable.generate("LOADREF 0");
            SymbolTable.generate("LOADREF "+method.getOffset());
            SymbolTable.generate("CALL");

        }else{//is static
            SymbolTable.generate("POP");
            if(!method_type.same_type(new VoidType()))
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

}
