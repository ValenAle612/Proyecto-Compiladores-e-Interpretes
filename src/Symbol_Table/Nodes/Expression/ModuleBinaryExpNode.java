package Symbol_Table.Nodes.Expression;

import Lexical_Analyzer.Token;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.ConcreteType;
import Symbol_Table.Types.IntType;

public class ModuleBinaryExpNode extends BinaryExpressionNode {

    public ModuleBinaryExpNode(Token token){
        super(token);
    }

    @Override
    public ConcreteType verify() throws SemanticException {
        if( left_side.verify().same_type(new IntType()) && right_side.verify().same_type(new IntType()) )
            return new IntType();
        else
            throw new SemanticException(token, "the binary operator "+token.getLexeme()+" it is only for integer type");
    }

    @Override
    public void generate() {
        left_side.generate();
        right_side.generate();
        SymbolTable.generate("MOD");
    }

    @Override
    public void create_binary_expression(ExpressionNode left_side, ExpressionNode right_side){
        this.left_side = left_side;
        this.right_side = right_side;
    }

}
