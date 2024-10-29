package Symbol_Table.Types;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClassType extends ConcreteType{

    private Token class_type_token;
    private boolean is_String;

    public ClassType(Token class_type_token){
        this.class_type_token = class_type_token;
        this.is_String = false;
    }

    public ClassType(Token class_type_token, boolean is_String){
        this.class_type_token = class_type_token;
        this.is_String = is_String;
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
    public void check_existence() throws SemanticException {
        if( !( SymbolTable.getInstance().class_exists( class_type_token.getLexeme() ) ) )
            throw new SemanticException( getClass_type_token(),
                    "the class "+ getClass_type_token().getLexeme() +" is undefined" );
    }

    @Override
    public boolean is_subtype_of(MethodType type) throws SemanticException {
        return type.verify_if_is_subtype(this);
    }

    public boolean same_type(ClassType type){
        return this.class_type_token.getLexeme().equals(type.getClass_type_token().getLexeme());
    }

    public boolean verify_if_is_subtype(ClassType subtype) throws SemanticException {
        boolean is_subtype = false;

        if(SymbolTable.getInstance().class_exists(subtype.getType_name())){
            ArrayList<String> ancestors = SymbolTable.getInstance().getClass(subtype.getType_name()).ancestors();
            if(ancestors.contains(this.class_type_token.getLexeme()))
                is_subtype = true;
        } else if(subtype.is_String){

            if(this.class_type_token.getLexeme().equals("String")){
                is_subtype = true;
            } else if ( class_type_token.getTokenType().equals(TokenId.literal_String)
                            && subtype.class_type_token.getTokenType().equals(TokenId.literal_String) ) {
                is_subtype = true;
            }

        }

        return is_subtype;
    }

}
