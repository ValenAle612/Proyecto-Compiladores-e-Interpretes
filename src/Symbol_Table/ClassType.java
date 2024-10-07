package Symbol_Table;

import Lexical_Analyzer.Token;

public class ClassType extends ConcreteType{

    private Token class_type_token;

    public ClassType(Token class_type_token){
        this.class_type_token = class_type_token;
    }

    public String getType_name(){
        return class_type_token.getLexeme();
    }

    public Token getClass_type_token(){
        return class_type_token;
    }

    @Override
    public String getCurrentType(){
        return class_type_token.getLexeme();
    }

    @Override
    public void check_existence() throws SemanticException{
        if( !( SymbolTable.getInstance().class_exists( class_type_token.getLexeme() ) ) )
            throw new SemanticException( getClass_type_token(),
                    "the class "+ getClass_type_token().getLexeme() +" is undefined" );
    }

}
