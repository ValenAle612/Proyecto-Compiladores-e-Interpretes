package Symbol_Table.Nodes.Expression;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.BooleanType;
import Symbol_Table.Types.Type;

public class OrBinaryExpNode extends BinaryExpressionNode {

    public OrBinaryExpNode(Token token){
        super(token);
    }

    @Override
    public Type verify() throws SemanticException{
        if(left_side.verify().same_type(new BooleanType()) && right_side.verify().same_type(new BooleanType()) )
            return new BooleanType();
        else
            throw new SemanticException(token, "binary operator "+ token.getLexeme() +" it is only for boolean type");
    }

    @Override
    public void create_binary_expression(ExpressionNode left_side, ExpressionNode right_side){
        this.left_side = left_side;
        this.right_side = right_side;
    }

}
