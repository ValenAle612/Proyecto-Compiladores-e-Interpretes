package Symbol_Table.Nodes.Expression;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.Type;

import java.beans.Expression;

public class UnaryExpressionNode extends ExpressionNode {

    protected Token token;
    protected OperandNode operandNode;

    public UnaryExpressionNode(Token token, OperandNode operandNode){
        this.token = token;
        this.operandNode = operandNode;
    }

    public Type verify() throws SemanticException{
        return null;
    }

}
