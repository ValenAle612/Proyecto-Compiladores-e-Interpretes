package Symbol_Table.Nodes.Literal;

import Symbol_Table.SymbolTable;
import Symbol_Table.Types.ClassType;
import Symbol_Table.SemanticException;
import Symbol_Table.Types.Type;
import Symbol_Table.Nodes.Expression.OperandNode;
import Lexical_Analyzer.Token;

public class StringNode extends OperandNode {

    protected Token token;

    public StringNode(Token token){
        this.token = token;
    }

    public Type verify() throws SemanticException{
        return new ClassType(token, true);
    }

    @Override
    public void generate() {
        String string = token.getLexeme().substring(1, token.getLexeme().length() - 1);

        SymbolTable.generate("RMEM 1 ; pointer to the String at .HEAP");
        SymbolTable.generate("PUSH " + (string.length() + 1));
        SymbolTable.generate("PUSH simple_malloc");
        SymbolTable.generate("CALL");

        for(int i = 0; i < string.length(); i++){
            SymbolTable.generate("DUP ; duplicate the reference at the"+
                    " beggining of the String since STOREREF consumes it");
            SymbolTable.generate("PUSH "+'\''+string.charAt(i)+'\''+" ");
            SymbolTable.generate("STOREREF "+i+" ; save the pushed letter");
        }

        SymbolTable.generate("DUP ; duplicate the reference at the"+
                " beggining of the String since STOREREF consumes it");
        SymbolTable.generate("PUSH "+0+" ; push the terminator");
        SymbolTable.generate("STOREREF "+string.length());
    }

    @Override
    public Token getToken() {
        return token;
    }

}
