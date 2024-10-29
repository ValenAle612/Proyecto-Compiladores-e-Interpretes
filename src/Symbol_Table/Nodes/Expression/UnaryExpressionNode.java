package Symbol_Table.Nodes.Expression;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.BooleanType;
import Symbol_Table.Types.IntType;
import Symbol_Table.Types.Type;

public class UnaryExpressionNode extends ExpressionNode {

    protected Token token;
    protected OperandNode operandNode;

    public UnaryExpressionNode(Token token, OperandNode operandNode){
        this.token = token;
        this.operandNode = operandNode;
    }

    @Override
    public Type verify() throws SemanticException{
        if(token != null){

            if(token.getLexeme().equals("+") || token.getLexeme().equals("-")){

                if(operandNode.verify().same_type(new IntType()))
                    return new IntType();
                else
                    throw new SemanticException(token, "integer type for unary operator was expected");

            } else {

                if(operandNode.verify().same_type(new BooleanType()))
                    return new BooleanType();
                else
                    throw new SemanticException(token, "boolean type for unary operator was expected");

            }

        } else
            return operandNode.verify();

    }

}
