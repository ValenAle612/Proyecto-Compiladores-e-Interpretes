package Symbol_Table;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Syntax_Analyzer.SyntaxException;

import java.util.*;

public class SymbolTable {

    private static SymbolTable instance;
    private static Map<String, ConcreteClass> classes;
    public static Class current_class;
    public static Method current_method;
    public static Attribute current_attribute;
    public static boolean is_there_a_main_class;

    private SymbolTable() throws SemanticException {

        classes = new HashMap<String, ConcreteClass>();
        is_there_a_main_class = false;

        Token object = new Token(TokenId.class_id, "Object", 0);
        ConcreteClass concreteClass_object = new ConcreteClass(object);
        concreteClass_object.setInherit_class_token(null);
        Method debug_print = new Method( new HashMap<String,Parameter>(), new ArrayList<Parameter>(),
                                         new Token(TokenId.method_var_id,"debug_print",0),
                                         TokenId.kw_static, new VoidType() );
        debug_print.save_parameter("i", new Parameter( new Token( TokenId.method_var_id, "i", 0 ),
                                                             new IntType() ) );
        concreteClass_object.save_method(debug_print);

        Token string_token = new Token(TokenId.class_id, "String", 0);
        ConcreteClass concreteClass_String = new ConcreteClass( string_token );
        concreteClass_String.setInherit_class_token(object);

        Token system_token = new Token(TokenId.class_id, "System", 0);
        ConcreteClass concreteClass_system = new ConcreteClass( system_token );
        concreteClass_system.setInherit_class_token(object);

        Method read = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                                  new Token(TokenId.method_var_id,"read",0),
                                  TokenId.kw_static, new IntType() );
        concreteClass_system.save_method(read);

        Method printB = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printB",0),
                TokenId.kw_static, new VoidType() );
        printB.save_parameter("b", new Parameter( new Token(TokenId.method_var_id, "b", 0),
                                                        new BooleanType() ));
        concreteClass_system.save_method(printB);

        Method printC = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printC",0),
                TokenId.kw_static, new VoidType() );
        printC.save_parameter("c", new Parameter( new Token(TokenId.method_var_id, "c", 0),
                new CharType() ));
        concreteClass_system.save_method(printC);

        Method printI = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printI",0),
                TokenId.kw_static, new VoidType() );
        printI.save_parameter("i", new Parameter( new Token(TokenId.method_var_id, "i", 0),
                new IntType() ));
        concreteClass_system.save_method(printI);

        Method printS = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printS",0),
                TokenId.kw_static, new VoidType() );
        Token tokenPrintS = new Token(TokenId.class_id, "String", 0);
        printS.save_parameter("s", new Parameter( new Token(TokenId.method_var_id, "s", 0),
                new ClassType(tokenPrintS) ));
        concreteClass_system.save_method(printS);

        Method println = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"println",0),
                TokenId.kw_static, new VoidType() );
        concreteClass_system.save_method(println);

        Method printBln = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printBln",0),
                TokenId.kw_static, new VoidType() );
        printBln.save_parameter("b", new Parameter( new Token(TokenId.method_var_id, "b", 0),
                new BooleanType() ));
        concreteClass_system.save_method(printBln);

        Method printIln = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printIln",0),
                TokenId.kw_static, new VoidType() );
        printIln.save_parameter("i", new Parameter( new Token(TokenId.method_var_id, "i", 0),
                new IntType() ));
        concreteClass_system.save_method(printIln);

        Method printCln = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printCln",0),
                TokenId.kw_static, new VoidType() );
        printCln.save_parameter("c", new Parameter( new Token(TokenId.method_var_id, "c", 0),
                new CharType() ));
        concreteClass_system.save_method(printCln);

        Method printSln = new Method( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printSln",0),
                TokenId.kw_static, new VoidType() );
        Token printSln_token = new Token(TokenId.class_id, "String", 0);
        printSln.save_parameter("s", new Parameter( new Token(TokenId.method_var_id, "s", 0),
                new ClassType(printSln_token) ));
        concreteClass_system.save_method(printSln);

        save_class(concreteClass_object, "Object");
        save_class(concreteClass_String, "String");
        save_class(concreteClass_system, "System");

    }

    public void save_class(ConcreteClass concrete_class, String class_name) throws SemanticException{
        if( classes.get(class_name) == null ){
          classes.put(class_name, concrete_class);
        }else
            throw new SemanticException( concrete_class.getToken(), "the class " + concrete_class.getToken().getLexeme()
                                                                                 + " already exists " );
    }

    public static SymbolTable getInstance() throws SemanticException {
        if( instance == null )
            instance = new SymbolTable();

        return instance;
    }

    public static Map<String, ConcreteClass> getClasses(){
        return classes;
    }

    public static void setClasses(Map<String, ConcreteClass> classes){
        SymbolTable.classes = classes;
    }

    public boolean class_exists(String class_name){
        ConcreteClass concreteClass = classes.get(class_name);
        if(concreteClass == null)
            return false;
        else return true;
    }

    public static void clean(){
        instance = null;
        classes = null;
        current_class = null;
        current_method = null;
        current_attribute = null;
        is_there_a_main_class = false;
    }

    public ConcreteClass getClass(String class_name){
        return classes.get(class_name);
    }

    public void is_well_stated() throws SemanticException, SyntaxException {
        for ( ConcreteClass concreteClass : classes.values() )
            concreteClass.is_well_stated();
    }

    public void consolidate() throws  SemanticException, SyntaxException {
        for( ConcreteClass concreteClass : classes.values() )
            concreteClass.consolidate();
    }

    public void exists_static_main_method() throws SemanticException {
        is_there_a_main_class = false;
        Token main_token = new Token( TokenId.method_var_id, "main", 0 );
        for( ConcreteClass concreteClass : classes.values() ){
            for( Method method : concreteClass.getMethods().values() ){
                if( method.getStatic_method() != null )
                    if( method.getMethod_type().getCurrentType().equals("void")
                            && method.getMethod_token().getLexeme().equals("main")
                            && method.getParameters().isEmpty() ){
                        is_there_a_main_class = true;
                        break;
                    }
            }
        }

        if( !is_there_a_main_class )
            throw new SemanticException( main_token, "No static main() method without parameters was declared." );

    }

}
