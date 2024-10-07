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

}
