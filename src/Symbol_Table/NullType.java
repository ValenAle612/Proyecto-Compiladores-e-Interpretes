package Symbol_Table;

public class NullType extends ConcreteType {

    public NullType(){ }

    @Override
    public String getCurrentType(){
        return "null";
    }

}
