package Symbol_Table.Nodes.Expression;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.BooleanType;
import Symbol_Table.Types.ConcreteType;
import Symbol_Table.Types.Type;

public class EqualsBinaryExpNode extends BinaryExpressionNode {

    public EqualsBinaryExpNode(Token token){
        super(token);
    }

    @Override
    public ConcreteType verify() throws SemanticException {
        Type left_side_type = left_side.verify();
        Type right_side_type = right_side.verify();
        if( left_side_type.is_subtype_of(right_side_type) || right_side_type.is_subtype_of(left_side_type) )
            return new BooleanType();
        else
            throw new SemanticException(token, "the binary operator "+token.getLexeme()+" only works with 'conforming' type");
    }

    @Override
    public void create_binary_expression(ExpressionNode left_side, ExpressionNode right_side){
        this.left_side = left_side;
        this.right_side = right_side;
    }

}
