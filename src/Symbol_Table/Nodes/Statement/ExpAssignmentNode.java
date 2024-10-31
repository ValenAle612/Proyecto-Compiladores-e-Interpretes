package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class ExpAssignmentNode extends AssignmentNode {

    protected Token token;
    protected ExpressionNode expressionNode;

    public ExpAssignmentNode(Token token, AccessNode accessNode){
        this.token = token;
        System.out.println("EXP ASSIGNMENT NODE "+(left_node == null));
        this.left_node = accessNode;
    }

    @Override
    public void verify() throws SemanticException{
        Type type = left_node.verify();

        if(!left_node.can_be_assigned()) {
            System.out.println("EXP ASSIGNMENT NODE can be assigned dio FALSE ");
            throw new SemanticException(token, "left side of the assignment must end in a variable");
        }

        Type expression_type = expressionNode.verify();
        if(!expression_type.is_subtype_of(type))
            throw new SemanticException(token, "expression of right side is not a subtype of left side");

    }

    @Override
    public void insert_expression(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }
}
