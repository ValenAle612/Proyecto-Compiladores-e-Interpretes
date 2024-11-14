package Symbol_Table.Nodes.Statement;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Types.MethodType;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.Type;
import Symbol_Table.Types.VoidType;
import Symbol_Table.Method;

public class ReturnNode extends StatementNode {

    protected Token token;
    protected ExpressionNode expressionNode;
    protected Method method;
    protected int number_of_local_variables;

    public ReturnNode(Token token){
        this.token = token;
    }

    public void insertExpression(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    @Override
    public void verify() throws SemanticException{
        MethodType type = SymbolTable.current_method.getMethod_type();
        method = SymbolTable.current_method;

        if(expressionNode == null) {
            if(!type.same_type(new VoidType()))
                throw new SemanticException(token, type.getCurrentType()+" type must be returned");
        }else{
            if(type.same_type(new VoidType()))
                throw new SemanticException(token, "trying to return in a void function");

            Type expression_type = expressionNode.verify();

            if(!expression_type.is_subtype_of(type))
                throw new SemanticException(token,"type of return expression is not a subtype of method return");
        }

        number_of_local_variables = SymbolTable.getCurrent_block().getLocal_variables().size();

    }

    @Override
    public void generate() {
        SymbolTable.generate("FMEM "+number_of_local_variables);

        if(method.getMethod_type().same_type(new VoidType())){//if is a void method
            SymbolTable.generate("STOREFP");
            SymbolTable.generate("RET "+method.getOffsetLine());
        }else{ //if is not
            expressionNode.generate();
            SymbolTable.generate("STORE "+method.getReturnOffset());
            SymbolTable.generate("STOREFP");
            SymbolTable.generate("RET "+method.getOffsetLine());
        }
    }

}
