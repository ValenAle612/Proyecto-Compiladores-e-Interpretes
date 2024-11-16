package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.IntType;
import Symbol_Table.Types.Type;

public class DecAssignmentNode extends AssignmentNode {

    protected Token token;

    public DecAssignmentNode(Token token, AccessNode accessNode){
        this.token = token;
        this.left_node = accessNode;
    }

    @Override
    public void verify() throws SemanticException{
        Type type = left_node.verify();

        if(!left_node.can_be_assigned())
            throw new SemanticException(token, "left side of decrement assignment must end in a variable");

        if(!type.same_type(new IntType()))
            throw new SemanticException(token, "type of the variable must be integer");

        if(expressionNode != null)
            if(!expressionNode.verify().same_type(new IntType()))
                throw new SemanticException(token, "right side of the assignment must be integer");

    }

    @Override
    public void generate() {
        left_node.generate();
        expressionNode.generate();
        SymbolTable.instruction_list.add("SUB ; DecAssignmentNode");
        left_node.set_as_left_side();
        left_node.generate();
    }

    @Override
    public void insert_expression(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }
}
