package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Types.MethodType;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import Symbol_Table.Types.VoidType;

public class ReturnNode extends StatementNode {

    protected Token token;
    protected ExpressionNode expressionNode;

    public ReturnNode(Token token){
        this.token = token;
    }

    public void insertExpression(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    @Override
    public void verify() throws SemanticException{
        MethodType type = SymbolTable.current_method.getMethod_type();

        if(expressionNode == null) {
            if(!type.same_type(new VoidType()))
                throw new SemanticException(token, type.getCurrentType()+" type must be returned");
        }else{
            if(type.same_type(new VoidType()))
                throw new SemanticException(token, "trying to return in a void function");

            Type expression_type = expressionNode.verify();
            System.out.println("RERTURN NODE expression type "+expression_type.getCurrentType());
            System.out.println("RERTURN NODE type "+type.getCurrentType());
            if(!expression_type.is_subtype_of(type))
                throw new SemanticException(token,"type of return expression is not a subtype of method return");
        }

    }

}
