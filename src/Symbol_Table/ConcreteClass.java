package Symbol_Table;

import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Expression.ExpressionNode;
import Symbol_Table.Types.Type;
import Syntax_Analyzer.SyntaxException;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcreteClass extends Class{

    private Token class_token;
    private Token inherit_class_token;
    private Map<String, Attribute> attributes;
    private Map<String, Method> methods;
    private HashMap<String, Token> extended_classes;
    private boolean is_consolidated;
    private boolean no_circular_inheritance;

    private Builder class_builder;

    private Map<Integer, Attribute> attributes_offset;
    private Map<Integer, Method> methods_offset;
    private int offsetVT, offsetCIR;

    public ConcreteClass(Token class_id){
        this.class_token = class_id;
        this.attributes = new HashMap<String, Attribute>();
        this.methods = new HashMap<String, Method>();
        this.extended_classes = new HashMap<>();
        this.class_builder = new Builder(this.class_token);
        this.no_circular_inheritance = false;
        this. is_consolidated = false;
        this.methods_offset = new HashMap<>();
        this.attributes_offset = new HashMap<>();

        if(class_token.getLexeme() == "Object"){
            is_consolidated = true;
            no_circular_inheritance = true;
            offsetVT = 0;
            offsetCIR  = 0;
        }

    }

    public void setToken(Token class_token){
        this.class_token = class_token;
    }

    public void setInherit_class_token(Token inherited_class_token){
        this.inherit_class_token = inherited_class_token;
    }

    public Token getInherit_class_token(){
        return this.inherit_class_token;
    }

    @Override
    public ArrayList<String> ancestors() throws SemanticException {
        ArrayList<String> ancestors = new ArrayList<>();
        ancestors.add(this.inherit_class_token.getLexeme());

        if (inherit_class_token != null){
            ancestors.add(inherit_class_token.getLexeme());
            if (!inherit_class_token.getLexeme().equals("Object")) {
                ancestors.addAll(SymbolTable.getInstance().getClass(inherit_class_token.getLexeme()).ancestors());
            }
        }

        return ancestors;

    }

    public void setAttributes(Map<String, Attribute> attributes){
        this.attributes = attributes;
    }

    public Map<String, Attribute> getAttributes(){
        return this.attributes;
    }

    public void setMethods(Map<String,Method> methods){
        this.methods = methods;
    }

    public Map<String, Method> getMethods(){
        return methods;
    }

    public void setClass_builder(Builder class_builder){
        this.class_builder = class_builder;
    }

    public Builder getClass_builder(){
        return this.class_builder;
    }

    public void setExtended_classes(HashMap<String, Token> extended_classes){
        this.extended_classes = extended_classes;
    }

    @Override
    public HashMap<String, Token> getExtended_classes(){
        return this.extended_classes;
    }

    @Override
    public Token getToken() {
        return this.class_token;
    }

    @Override
    public void save_attribute(Attribute attribute) throws SemanticException {
        if( attributes.get( attribute.getAttribute_token().getLexeme() ) == null ){
            attributes.put(attribute.getAttribute_token().getLexeme(), attribute);
        } else
            throw  new SemanticException(attribute.getAttribute_token(),
                    "An attribute with the same name already exists: " + "'" + attribute.getAttribute_token().getLexeme() +
                            attribute.getAttribute_token().getLexeme() + "'" +
                            " within the same class -> " + class_token.getLexeme() );
    }

    @Override
    public void save_method(Method method) throws SemanticException {
        if( methods.get( method.getMethod_token().getLexeme() ) == null ){
            methods.put( method.getMethod_token().getLexeme(), method );
        } else
            throw new SemanticException( method.getMethod_token(), "A method with the same name already exists -> "
                    + class_token.getLexeme() );
    }

    @Override
    public void is_well_stated() throws SemanticException, SyntaxException {

        SymbolTable.current_class = this;

        if( !this.class_token.getLexeme().equals("Object") ){

            if( SymbolTable.getInstance().class_exists( this.inherit_class_token.getLexeme() ) ){

                for( Method method : this.methods.values() ){
                    method.is_well_stated();
                }

                for( Attribute attribute : this.attributes.values() ){
                    attribute.is_well_stated();
                }

                this.class_builder.is_well_stated();

            }else
                throw new SemanticException( this.inherit_class_token,
                        this.inherit_class_token.getLexeme() + " class " +
                                this.class_token.getLexeme() + "  tries to inherit from an undefined class -> " +
                                this.inherit_class_token.getLexeme() );

        }
    }

    @Override
    public void save_extends(String lexeme, Token token) throws SemanticException {
        if( extended_classes.get(token.getLexeme() ) == null)
            extended_classes.put(token.getLexeme(), token);
        else
            throw new SemanticException(token, "the class "+ token.getLexeme() +" is already in the class "+ class_token.getLexeme() +" signature");
    }

    @Override
    public boolean is_concrete_class() {
        return true;
    }

    @Override
    public Method getMethod(String name) {
        return methods.get(name);
    }

    @Override
    public Method conformance(String method_name, List<ExpressionNode> current_parameters) throws SemanticException {
        List<Type> current_parameters_type_list = new ArrayList<>();
        for(ExpressionNode current_parameter : current_parameters)
            current_parameters_type_list.add(current_parameter.verify());

        for(Method method : methods.values())
            if(method.getMethod_token().getLexeme().equals(method_name)
                    && method.conformance(current_parameters_type_list))
                return method;

        return null;
    }

    @Override
    public Attribute getAttribute(String name) {
        return this.attributes.get(name);
    }

    public void consolidate() throws SemanticException, SyntaxException{

        check_circular_inheritance(new ArrayList<ConcreteClass>());

        if( !is_consolidated ){

            ConcreteClass concrete_class_p = SymbolTable.getInstance().getClass( inherit_class_token.getLexeme() );

            if(concrete_class_p.is_consolidated){

                for(Method method : concrete_class_p.getMethods().values()){
                    if( methods.get( method.getMethod_token().getLexeme() ) == null ) {
                        this.save_method(method);
                    }else{
                        Method self_method = methods.get( method.getMethod_token().getLexeme() );

                        if( !method.is_method_equal( self_method ) )
                            throw new SemanticException( self_method.getMethod_token(),
                                    "the method "+ method.getMethod_token().getLexeme() +
                                    " was declared with a different signature in "+ class_token.getLexeme() );
                    }
                }

                for( Attribute attribute : concrete_class_p.attributes.values() ){
                    if( attributes.get( attribute.getAttribute_token().getLexeme() ) == null )
                        this.save_attribute(attribute);
                    else
                        throw new SemanticException( attributes.get( attribute.getAttribute_token().getLexeme() ).getAttribute_token(),
                                "An attribute with the same name already exists");
                }

                this.is_consolidated = true;

            }else{
                concrete_class_p.consolidate();
                this.consolidate();
            }

        }

    }

    public void statement_check() throws SemanticException {
        SymbolTable.current_class = this;
        for( Method method : methods.values() )
            if( method.getAssociated_class().getLexeme().equals(this.class_token.getLexeme()) )
                method.statement_check();
    }

    private void check_circular_inheritance(ArrayList<ConcreteClass> parent_class) throws SemanticException {
        boolean circular_inheritance = !no_circular_inheritance ;

        if( circular_inheritance ){

            if( parent_class.contains(this) )
                throw new SemanticException( this.inherit_class_token,
                        "the class "+ this.getToken().getLexeme() +" that posses circular inheritance ");
            else{
                parent_class.add(this);
                ConcreteClass concreteClass = SymbolTable.getInstance().getClass( inherit_class_token.getLexeme() );

                if(concreteClass != null){
                    concreteClass.check_circular_inheritance(parent_class);
                    no_circular_inheritance = true;
                }

            }
        }

        parent_class.remove(this);

    }

    public int getOffsetVT(){
        return offsetVT;
    }

    public void setOffsetVT(int offsetVT){
        this.offsetVT = offsetVT;
    }

    public int getOffsetCIR(){
        return offsetCIR;
    }

    public void setOffsetCIR(int offsetCIR){
        this.offsetCIR = offsetCIR;
    }

    @Override
    public void generate(){
        SymbolTable.current_class = this;
        generate_data();
        generate_code();
    }

    private void generate_data(){
        SymbolTable.generate(".DATA");
        String VT_labels;
        if(methods_offset.size() != 0) {
            VT_labels = "VT_" + class_token.getLexeme() + ": DW";
            for (int offset = 0; offset < methods_offset.size(); offset++) {
                Method method = methods_offset.get(offset);
                VT_labels += method.method_label() + ",";
            }

            VT_labels = VT_labels.substring(0, VT_labels.length() - 1);
        }else{
            VT_labels = "VT_"+class_token.getLexeme()+": NOP";
        }

        SymbolTable.generate(VT_labels);
    }

    private void generate_code() {
        SymbolTable.generate(".CODE");
        String method_code_label;
        for (Method method : methods.values()){
            if ( method.getAssociated_class().getLexeme().equals( this.class_token.getLexeme() ) ) {
                method_code_label = method.method_label()+":";

                SymbolTable.generate(method_code_label);
                method.generate();

                SymbolTable.generate("");
            }
        }
    }

    private void setMethodsOffset(ConcreteClass concrete_class){
        this.offsetVT = concrete_class.getOffsetVT();
        for(Method method : methods.values()){
            if(method.is_dynamic()){
                if(method.getOffset() == -1){
                    method.setOffset(offsetVT);
                    offsetVT++;
                }

                methods_offset.put(method.getOffset(), method);
            }
        }
    }

    private void setAttributesOffset(ConcreteClass concrete_class){
        this.offsetCIR = concrete_class.getOffsetCIR();
        for(Attribute attribute : attributes.values()){
            if(attribute.getOffset() == -1){
                attribute.setOffset(offsetCIR);
                offsetCIR++;
            }

            attributes_offset.put(attribute.getOffset(), attribute);
        }
    }

}
