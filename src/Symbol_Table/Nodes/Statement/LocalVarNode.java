package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Nodes.Statement.StatementNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import Symbol_Table.VarEntry;

public class LocalVarNode extends StatementNode implements VarEntry {

    protected Token token;
    protected ExpressionNode expressionNode;
    protected Type type;

    protected int offset;

    public LocalVarNode(Token token){
        this.token = token;
        this.offset = -1;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public Token getToken(){
        return token;
    }

    public void setExpressionNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode(){
        return expressionNode;
    }

    @Override
    public void verify() throws SemanticException{
        if(this.expressionNode != null)
            this.set_type(expressionNode.verify());

        for(BlockNode blockNode : SymbolTable.block_stack) {
            if(blockNode.getLocalVariable(token.getLexeme()) != null)
                throw new SemanticException(token, "variable already declared");
        }

        if(expressionNode.verify().getCurrentType().equals("void"))
            throw new SemanticException(token, "is not possible assign something with void type to the variable "+token.getLexeme());

        SymbolTable.getCurrent_block().insertLocalVariable(this);

        if(expressionNode.verify().getCurrentType().equals("null"))
            throw new SemanticException(token, "is not possible assign something with null type to the variable "+token.getLexeme());

    }

    @Override
    public void generate() {
        SymbolTable.instruction_list.add("RMEM 1; Saving space for local variable "+token.getLexeme());
        if(expressionNode != null) {
            expressionNode.generate();
            SymbolTable.generate("STORE "+offset);
        }
    }

    public void set_type(Type type){
        this.type = type;
    }

    @Override
    public boolean is_attribute() {
        return false;
    }

    @Override
    public Type get_type() {
        return type;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }
}
