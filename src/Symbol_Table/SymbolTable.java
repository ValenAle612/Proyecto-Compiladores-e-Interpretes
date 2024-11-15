package Symbol_Table;

import Lexical_Analyzer.Token;
import Lexical_Analyzer.TokenId;
import Symbol_Table.Nodes.Statement.BlockNode;
import Symbol_Table.Nodes.Statement.LocalVarNode;
import Symbol_Table.Types.*;
import Symbol_Table.Generate_Presets.*;
import Syntax_Analyzer.SyntaxException;

import java.util.*;

public class SymbolTable {

    private static SymbolTable instance;
    private static Map<String, ConcreteClass> classes;
    public static Class current_class;
    public static Method current_method;
    public static Attribute current_attribute;

    public static boolean is_there_a_main_class;
    public static Method main_method;

    public static List<BlockNode> block_stack;
    public static List<String> instruction_list;

    private SymbolTable() throws SemanticException {

        classes = new HashMap<String, ConcreteClass>();
        block_stack = new ArrayList<>();
        instruction_list = new ArrayList<>();
        is_there_a_main_class = false;

        Token object = new Token(TokenId.class_id, "Object", 0);
        ConcreteClass concreteClass_object = new ConcreteClass(object);
        concreteClass_object.setInherit_class_token(null);
        Method debugPrint = new DebugPrintMethod( new HashMap<String,Parameter>(), new ArrayList<Parameter>(),
                                         new Token(TokenId.method_var_id,"debugPrint",0),
                                         TokenId.kw_static, new VoidType(), object);
        debugPrint.save_parameter("i", new Parameter( new Token( TokenId.method_var_id, "i", 0 ),
                                                             new IntType() ) );
        concreteClass_object.save_method(debugPrint);
        debugPrint.setAssociated_class(concreteClass_object.getToken());

        Token string_token = new Token(TokenId.class_id, "String", 0);
        ConcreteClass concreteClass_String = new ConcreteClass( string_token );
        concreteClass_String.setInherit_class_token(object);

        Token system_token = new Token(TokenId.class_id, "System", 0);
        ConcreteClass concreteClass_system = new ConcreteClass( system_token );
        concreteClass_system.setInherit_class_token(object);

        Method read = new ReadMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                                  new Token(TokenId.method_var_id,"read",0),
                                  TokenId.kw_static, new IntType(), system_token );
        concreteClass_system.save_method(read);
        read.setAssociated_class(concreteClass_system.getToken());

        Method printB = new PrintBMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printB",0),
                TokenId.kw_static, new VoidType(), system_token );
        printB.save_parameter("b", new Parameter( new Token(TokenId.method_var_id, "b", 0),
                                                        new BooleanType() ));
        concreteClass_system.save_method(printB);
        printB.setAssociated_class(concreteClass_system.getToken());

        Method printC = new PrintCMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printC",0),
                TokenId.kw_static, new VoidType(), system_token );
        printC.save_parameter("c", new Parameter( new Token(TokenId.method_var_id, "c", 0),
                new CharType() ));
        concreteClass_system.save_method(printC);
        printC.setAssociated_class(concreteClass_system.getToken());

        Method printI = new PrintIMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printI",0),
                TokenId.kw_static, new VoidType(), system_token );
        printI.save_parameter("i", new Parameter( new Token(TokenId.method_var_id, "i", 0),
                new IntType() ));
        concreteClass_system.save_method(printI);
        printI.setAssociated_class(concreteClass_system.getToken());

        Method printS = new PrintSMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printS",0),
                TokenId.kw_static, new VoidType(), system_token );
        Token tokenPrintS = new Token(TokenId.class_id, "String", 0);
        printS.save_parameter("s", new Parameter( new Token(TokenId.method_var_id, "s", 0),
                new ClassType(tokenPrintS) ));
        concreteClass_system.save_method(printS);
        printS.setAssociated_class(concreteClass_system.getToken());

        Method println = new PrintlnMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"println",0),
                TokenId.kw_static, new VoidType(), system_token );
        concreteClass_system.save_method(println);
        println.setAssociated_class(concreteClass_system.getToken());

        Method printBln = new PrintBlnMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printBln",0),
                TokenId.kw_static, new VoidType(), system_token );
        printBln.save_parameter("b", new Parameter( new Token(TokenId.method_var_id, "b", 0),
                new BooleanType() ));
        concreteClass_system.save_method(printBln);
        printBln.setAssociated_class(concreteClass_system.getToken());

        Method printIln = new PrintIlnMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printIln",0),
                TokenId.kw_static, new VoidType(), system_token );
        printIln.save_parameter("i", new Parameter( new Token(TokenId.method_var_id, "i", 0),
                new IntType() ));
        concreteClass_system.save_method(printIln);
        printIln.setAssociated_class(concreteClass_system.getToken());

        Method printCln = new PrintClnMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printCln",0),
                TokenId.kw_static, new VoidType(), system_token);
        printCln.save_parameter("c", new Parameter( new Token(TokenId.method_var_id, "c", 0),
                new CharType() ));
        concreteClass_system.save_method(printCln);
        printCln.setAssociated_class(concreteClass_system.getToken());

        Method printSln = new PrintSlnMethod( new HashMap<String, Parameter>(), new ArrayList<Parameter>(),
                new Token(TokenId.method_var_id,"printSln",0),
                TokenId.kw_static, new VoidType(), system_token);
        Token printSln_token = new Token(TokenId.class_id, "String", 0);
        printSln.save_parameter("s", new Parameter( new Token(TokenId.method_var_id, "s", 0),
                new ClassType(printSln_token) ));
        concreteClass_system.save_method(printSln);
        printSln.setAssociated_class(concreteClass_system.getToken());

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
        else
            return true;
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

    public static void save_current_block(BlockNode blockNode){
        block_stack.addFirst(blockNode);
    }

    public static BlockNode delete_current_block(){
        return block_stack.removeFirst();
    }

    public static BlockNode getCurrent_block(){
        return block_stack.get(0);
    }

    public void statement_check() throws SemanticException {
        for(ConcreteClass class_ : classes.values())
            class_.statement_check();
    }

    public LocalVarNode get_local_variable_of_method( String local_var_name ){
        LocalVarNode localVarNode = null;

        for(BlockNode blockNode: block_stack){
            localVarNode = blockNode.getLocalVariable(local_var_name);
        }

        return localVarNode;
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
                        main_method = method;
                        break;
                    }
            }
        }

        if( !is_there_a_main_class )
            throw new SemanticException( main_token, "No static main() method without parameters was declared." );

    }

    public void generate_code() throws SemanticException{
        initial_generate();

        for(ConcreteClass concreteClass : classes.values())
            concreteClass.generate();
    }

    private void initial_generate(){
        //code
        instruction_list.add(".CODE");
        instruction_list.add("PUSH simple_heap_init");
        instruction_list.add("CALL");
        instruction_list.add("PUSH "+
                main_method.getAssociated_class().getLexeme()+
                main_method.getMethod_token().getLexeme());
        instruction_list.add("CALL");
        instruction_list.add("HALT");
        instruction_list.add("");

        instruction_list.add("simple_heap_init:");
        instruction_list.add("RET 0");
        instruction_list.add("");

        //malloc
        instruction_list.add("simple_malloc:");
        instruction_list.add("LOADFP ; Unit initialization");
        instruction_list.add("LOADSP");
        instruction_list.add("STOREFP ; Ends RA initialization");
        instruction_list.add("LOADHL ; hl");
        instruction_list.add("DUP ; hl");
        instruction_list.add("PUSH 1 ; 1");
        instruction_list.add("ADD ; hl + 1");
        instruction_list.add("STORE 4 ; Stores result (pointer to the base of the block)");
        instruction_list.add("LOAD 3 ; Loads the number of cells to allocate (parameter)");
        instruction_list.add("ADD");
        instruction_list.add("STOREHL ; Moves the heap limit (hl)");
        instruction_list.add("STOREFP");
        instruction_list.add("RET 1 ; Returns, removing the parameter");
        instruction_list.add("");
    }

    public static void generate(String instruction){
        instruction_list.add(instruction);
    }

}
