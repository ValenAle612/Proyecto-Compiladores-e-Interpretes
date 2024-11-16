package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.IntType;
import Symbol_Table.Nodes.Expression.OperandNode;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class IntNode extends OperandNode {

    protected Token token;

    public IntNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return new IntType();
    }

    @Override
    public void generate() {
        SymbolTable.generate("PUSH "+token.getLexeme()+" ; push integer");
    }

    @Override
    public Token getToken() {
        return token;
    }

}
