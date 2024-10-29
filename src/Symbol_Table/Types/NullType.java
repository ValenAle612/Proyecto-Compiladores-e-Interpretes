package Symbol_Table.Types;

import Symbol_Table.SemanticException;

public class NullType extends ConcreteType {

    public NullType(){ }

    @Override
    public String getCurrentType(){
        return "null";
    }

    @Override
    public boolean is_subtype_of(MethodType type) throws SemanticException {
        return type instanceof ClassType;
    }

    public boolean same_type(Type type){
        return type instanceof ClassType;
    }

}
