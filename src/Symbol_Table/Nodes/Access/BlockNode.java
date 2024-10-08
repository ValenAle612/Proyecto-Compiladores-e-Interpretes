package Symbol_Table.Nodes.Access;

import Symbol_Table.Nodes.LocalVarNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Nodes.Statement.StatementNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockNode extends StatementNode {

    private List<StatementNode> statements;
    private Map<String, LocalVarNode> local_variables;

    public BlockNode(){
        statements = new ArrayList<>();
        local_variables = new HashMap<>();
    }

    public void insert_statement(StatementNode statementNode){
        statements.add(statementNode);
    }

    public LocalVarNode getLocalVariable(String local_variable){
        return local_variables.get(local_variable);
    }

    public void insertLocalVariable(LocalVarNode localVarNode){
        //TO-DO
    }

    @Override
    public void verify() throws SemanticException {

    }

}
