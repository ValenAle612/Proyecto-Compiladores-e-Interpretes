package Symbol_Table.Types;

import Symbol_Table.SemanticException;

public class IntType extends PrimitiveType{


    public IntType(){ }

    @Override
    public String getCurrentType(){
        return "int";
    }

    @Override
    public boolean is_subtype_of(MethodType type) throws SemanticException {
        return type.verify_if_is_subtype(this);
    }

    public boolean verify_if_is_subtype(IntType type){
        return true;
    }

}
