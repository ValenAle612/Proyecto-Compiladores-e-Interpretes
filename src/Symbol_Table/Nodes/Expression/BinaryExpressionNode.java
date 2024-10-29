package Symbol_Table.Nodes.Expression;

import Lexical_Analyzer.Token;

public abstract class BinaryExpressionNode extends ExpressionNode {

    protected Token token;
    protected ExpressionNode left_side;
    protected ExpressionNode right_side;

    public BinaryExpressionNode(Token token){
        this.token = token;
    }

    public abstract void create_binary_expression(ExpressionNode left_side, ExpressionNode right_side);

}
