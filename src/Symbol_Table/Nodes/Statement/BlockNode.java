package Symbol_Table.Nodes.Statement;

import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockNode extends StatementNode {

    private List<StatementNode> statements;
    private Map<String, LocalVarNode> local_variables;

    private int local_var_offset;

    public BlockNode(){
        statements = new ArrayList<>();
        local_variables = new HashMap<>();
    }

    public void insert_statement(StatementNode statementNode){
        statements.add(statementNode);
    }

    public List<StatementNode> getStatements(){
        return this.statements;
    }

    public Map<String, LocalVarNode> getLocal_variables(){
        return this.local_variables;
    }

    public LocalVarNode getLocalVariable(String local_variable){
        return local_variables.get(local_variable);
    }

    public void insertLocalVariable(LocalVarNode localVarNode) throws SemanticException {

        if(SymbolTable.getInstance().get_local_variable_of_method(localVarNode.getToken().getLexeme()) != null )
            throw new SemanticException(localVarNode.getToken(), "variable is already declared in this block or in a superior block");

        if(SymbolTable.current_method.getParameter(localVarNode.getToken().getLexeme()) != null)
            throw new SemanticException(localVarNode.getToken(), "variable is duplicated, it already exists a parameter with same name");

        localVarNode.setOffset(local_var_offset);
        local_var_offset--;

        local_variables.put(localVarNode.getToken().getLexeme(), localVarNode);

    }

    @Override
    public void verify() throws SemanticException {
        SymbolTable.save_current_block(this);

        for(StatementNode statement : statements) {
            statement.verify();
        }

        SymbolTable.delete_current_block();
    }

    @Override
    public void generate(){
        SymbolTable.save_current_block(this);

        for(StatementNode statementNode : statements)
            statementNode.generate();

        SymbolTable.delete_current_block();
        SymbolTable.generate("FMEM "+local_variables.size()+
                " ; free space for local variables, "
                + local_variables.size() +
                " memory spaces were freed ");
    }

    public int getLocal_var_offset(){
        return local_var_offset;
    }

    public void setLocal_var_offset(int local_var_offset){
        this.local_var_offset = local_var_offset;
    }

}
