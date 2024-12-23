package Symbol_Table.Nodes.Access;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.Attribute;
import Symbol_Table.Class;
import Symbol_Table.Nodes.Statement.LocalVarNode;
import Symbol_Table.Nodes.Statement.BlockNode;
import Symbol_Table.Parameter;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import java.util.List;

public class VarAccessNode extends AccessNode{

    protected Token token;
    protected LocalVarNode localVarNode;
    protected Attribute attribute;
    protected Parameter formal_parameter;

    public VarAccessNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{

        Type variable_type;
        this.localVarNode = null;

        for(BlockNode blockNode: SymbolTable.block_stack){
            localVarNode = blockNode.getLocalVariable(token.getLexeme());
            if(localVarNode != null)
                break;
        }

        if(localVarNode != null)
            variable_type = localVarNode.get_type();
        else{

            formal_parameter = SymbolTable.current_method.getParameter(token.getLexeme());
            if(formal_parameter != null)
                variable_type = formal_parameter.get_type();
            else{
                attribute = SymbolTable.current_class.getAttribute(token.getLexeme());
                if(attribute != null) {
                    verify_is_not_private();
                    if(SymbolTable.current_method.getStatic_method() == null)
                        variable_type = attribute.get_type();
                    else
                        throw new SemanticException(token, "it cannot access an instance attribute in a static method");
                } else
                    throw new SemanticException(token, "variable "+token.getLexeme()+"is not declared or is not accessible");
            }

        }

        if(chainedNode == null)
            return variable_type;
        else
            return chainedNode.verify(variable_type);

    }

    private void verify_is_not_private() throws SemanticException {
        Class class_ = SymbolTable.current_class;
        if(class_ != null) {

            Token inherited_class = class_.getInherit_class_token();
            if(inherited_class != null){

                Class inherit_class = SymbolTable.getInstance().getClass(inherited_class.getLexeme());
                Attribute attribute = inherit_class.getAttribute(this.token.getLexeme());
                if(attribute != null && attribute.getVisibility() == TokenId.kw_private)
                    throw new SemanticException(token, "attribute "+token.getLexeme()+" is not declared in a parent class and is private");

            }

        }
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

        if( chainedNode != null ) {
            return chainedNode.can_be_assigned();
        }else{

            Attribute attribute = SymbolTable.current_class.getAttribute(this.token.getLexeme());
            List<Parameter> parameters = SymbolTable.current_method.getParameters_list();
            BlockNode block = SymbolTable.current_method.getBlockNode();

            if(block.getLocal_variables().get(token.getLexeme()) != null)
                return true;

            for(Parameter parameter : parameters){
                if( parameter.getParameter_token().getLexeme().equals(this.token.getLexeme()) ){
                    return true;
                }
            }



            return attribute != null;
        }

    }

    @Override
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public void generate() {

        if(attribute != null){

            SymbolTable.generate("LOAD 3");

            if(!is_left_side_assignable || chainedNode != null){
                SymbolTable.generate("LOADREF "+attribute.getOffset());
            }else{
                SymbolTable.generate("SWAP");
                SymbolTable.generate("STOREREF "+attribute.getOffset());
            }

        }else if(formal_parameter != null){

            if(!is_left_side_assignable || chainedNode != null)
                SymbolTable.generate("LOAD "+formal_parameter.getOffset()
                        +" ; parameter "+formal_parameter.getParameter_token().getLexeme());
            else
                SymbolTable.generate("STORE "+formal_parameter.getOffset()
                        +" ; parameter "+formal_parameter.getParameter_token().getLexeme());

        }else{

            if(!is_left_side_assignable || chainedNode != null)
                SymbolTable.generate("LOAD "+localVarNode.getOffset()
                        +" ; local variable "+localVarNode.getToken().getLexeme());
            else
                SymbolTable.generate("STORE "+localVarNode.getOffset()
                        +" ; local variable "+localVarNode.getToken().getLexeme());

        }

        if(chainedNode != null) {
            chainedNode.set_same_side(is_left_side_assignable);
            chainedNode.generate();
        }

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

    public LocalVarNode getLocalVarNode() {
        return localVarNode;
    }
}