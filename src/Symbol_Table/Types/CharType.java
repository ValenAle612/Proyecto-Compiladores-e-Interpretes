package Symbol_Table.Types;

import Symbol_Table.SemanticException;

public class CharType extends PrimitiveType{

    public CharType(){ }

    @Override
    public String getCurrentType(){
        return "char";
    }

    @Override
    public boolean is_subtype_of(MethodType type) throws SemanticException {
        return type.verify_if_is_subtype(this);
    }

    public boolean verify_if_is_subtype(CharType charType){
        return true;
    }

}
