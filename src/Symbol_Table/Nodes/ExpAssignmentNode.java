package Symbol_Table.Nodes;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Access.AccessNode;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.SemanticException;

public class ExpAssignmentNode extends AssignmentNode {

    protected Token token;
    protected AccessNode accessNode;
    protected ExpressionNode expressionNode;

    public ExpAssignmentNode(Token token, AccessNode accessNode, ExpressionNode expressionNode){
        this.token = token;
        this.accessNode = accessNode;
        this.expressionNode = expressionNode;
    }

    public void verify() throws SemanticException{

    }

}
