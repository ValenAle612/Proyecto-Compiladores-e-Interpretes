package Symbol_Table;

public class VoidType extends MethodType {

    public VoidType(){ }

    @Override
    public String getCurrentType() {
        return "void";
    }

    public boolean verify_type(VoidType voidType){
        return true;
    }

}
