package Symbol_Table;

import Lexical_Analyzer.TokenId;
import Lexical_Analyzer.Token;
import Symbol_Table.Nodes.Statement.BlockNode;
import Symbol_Table.Types.MethodType;
import Symbol_Table.Types.Type;

import java.util.*;

public class Method {

    private HashMap<String, Parameter> parameters;
    private ArrayList<Parameter> parameters_list;
    private Token method_token;
    private TokenId static_method;
    private MethodType method_type;

    private Token associated_class;
    private BlockNode blockNode;

    public Method(HashMap<String, Parameter> parameters, ArrayList<Parameter> parameters_list, Token method_token, TokenId static_method,
                  MethodType method_type, Token associated_class){
        this.parameters = parameters;
        this.parameters_list = parameters_list;
        this.method_token = method_token;
        this.static_method = static_method;
        this.method_type = method_type;
        this.associated_class = associated_class;
        this.blockNode = new BlockNode();
    }

    public Token getAssociated_class(){
        return associated_class;
    }

    public void setParameters(HashMap<String,Parameter> parameters){
        this.parameters = parameters;
    }

    public HashMap<String, Parameter> getParameters(){
        return parameters;
    }

    public Parameter getParameter(String parameter){
        return parameters.get(parameter);
    }

    public void setParameters_list(ArrayList<Parameter> parameters_list){
        this.parameters = parameters;
    }

    public ArrayList<Parameter> getParameters_list(){
        return parameters_list;
    }

    public void setMethod_token(Token method_token){
        this.method_token = method_token;
    }

    public Token getMethod_token(){
        return method_token;
    }

    public void setStatic_method(TokenId static_method){
        this.static_method = static_method;
    }

    public TokenId getStatic_method(){
        return static_method;
    }

    public void setMethod_type(MethodType method_type){
        this.method_type = method_type;
    }

    public MethodType getMethod_type(){
        return method_type;
    }

    public void save_parameter(String name, Parameter parameter) throws SemanticException {
        if( parameters.get(name) == null ){
            parameters.put(name, parameter);
            parameters_list.add(parameter);
        }else
            throw new SemanticException(parameter.getParameter_token(), "another parameter already exists with the same name");
    }

    public void is_well_stated() throws SemanticException{
        if(method_type != null)
            method_type.check_existence();

        for( Parameter parameter : parameters.values() )
            parameter.is_well_stated();

    }

    public boolean is_method_equal(Method method){
        boolean is_same_method = this.method_token.getLexeme().equals( method.getMethod_token().getLexeme() );
        boolean are_same_parameters = this.compare_parameters( method.getParameters_list() );
        boolean is_same_static_modifier = this.static_method == method.getStatic_method();
        boolean is_same_method_type = this.method_type.getCurrentType().equals( method.getMethod_type().getCurrentType() );

        return is_same_method && are_same_parameters &&  is_same_static_modifier && is_same_method_type;
    }

    private boolean compare_parameters(ArrayList<Parameter> another_method_parameters){
        boolean are_equals = true;

        if( this.parameters_list.size() != another_method_parameters.size() ){
            are_equals = false;
        } else {
            for( int i = 0; i < another_method_parameters.size() ; i++ ){
                are_equals = this.verify_parameter( this.parameters_list.get(i), another_method_parameters.get(i) );
                if( !are_equals )
                    break;
            }
        }

        return are_equals;
    }

    public boolean verify_parameter(Parameter parameter_x, Parameter parameter_y){
        return parameter_x.getParameter_type().getCurrentType().equals( parameter_y.getParameter_type().getCurrentType() );
    }

    public void insert_block(BlockNode blockNode){
        this.blockNode = blockNode;
    }

    public BlockNode getBlockNode(){
        return blockNode;
    }

    public void statement_check() throws SemanticException {
        SymbolTable.current_method = this;
        blockNode.verify();
    }

    public boolean conformance( List<Type> current_parameters_type_list ) throws SemanticException {
        List<Type> formal_parameters_type_list = new ArrayList<>();

        for( Parameter formalParameter : parameters_list )
            formal_parameters_type_list.add( formalParameter.getParameter_type() );

        int i = 0;
        boolean comforming_parameters = true;
        if( formal_parameters_type_list.size() == current_parameters_type_list.size() ){

            for( Type formalType : formal_parameters_type_list ){
                Type currentType = current_parameters_type_list.get(i);
                i++;
                if( !currentType.is_subtype_of(formalType) ){
                    comforming_parameters = false;
                    break;
                }
            }

        } else
            comforming_parameters = false;

        return comforming_parameters;

    }

}
