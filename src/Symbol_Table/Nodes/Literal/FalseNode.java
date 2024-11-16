package Symbol_Table.Nodes.Literal;

import Lexical_Analyzer.Token;
import Symbol_Table.SymbolTable;
import Symbol_Table.Types.BooleanType;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;

public class FalseNode extends BooleanNode {

    protected Token token;

    public FalseNode(Token token) {
        this.token = token;
    }

    public Type verify() throws SemanticException {
        return new BooleanType();
    }

    @Override
    public void generate() {
        SymbolTable.generate("PUSH "+0+" ; push false");
    }

    @Override
    public Token getToken() {
        return token;
    }

}
