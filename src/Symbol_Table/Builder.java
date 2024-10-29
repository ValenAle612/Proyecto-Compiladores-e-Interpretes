package Symbol_Table;

import Lexical_Analyzer.Token;

public class Builder {

    private Token builder_token;

    public Builder(Token builder_token){
        this.builder_token = builder_token;
    }

    public void setBuilder_token(Token builder_token){
        this.builder_token = builder_token;
    }

    public Token getBuilder_token(){
        return this.builder_token;
    }

    public void is_well_stated() throws SemanticException {
        if( SymbolTable.getInstance().getClass(this.builder_token.getLexeme()) == null )
            throw new SemanticException(builder_token, "there is no class for the builder method "
                    + builder_token.getLexeme());
    }

}
