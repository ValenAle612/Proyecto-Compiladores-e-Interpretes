package Main_Controller;

import Lexical_Analyzer.*;
import Source_Manager.SourceManager;
import Source_Manager.SourceManagerImpl;
import Symbol_Table.SemanticException;
import Symbol_Table.SymbolTable;
import Syntax_Analyzer.SyntaxAnalyzer;
import Syntax_Analyzer.SyntaxException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main( String[] args ){
        try{
            String inputFile = args[0];
            String outputFile = args[1];

            boolean errors = false;

            if( args.length > 2 ){
                System.out.println("Incorrect number of parameters");
            }else{
                try{

                    SourceManager sourceManager = new SourceManagerImpl();
                    sourceManager.open(inputFile);
                    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
                    SymbolTable.getInstance();
                    SymbolTable.clean();
                    SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);

                    SymbolTable.getInstance().is_well_stated();
                    SymbolTable.getInstance().consolidate();
                    SymbolTable.getInstance().exists_static_main_method();
                    SymbolTable.getInstance().statement_check();

                    SymbolTable.getInstance().generate_code();
                    create_file(outputFile);

                    SymbolTable.clean();

                    if(!errors)
                        System.out.println( "Successful build\n \n[SinErrores]" );

                }catch ( FileNotFoundException e ){
                    System.out.println("ERROR: file not found");
                }catch ( IOException e ){
                    e.printStackTrace();
                } catch (LexicalException | SyntaxException | SemanticException e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch( ArrayIndexOutOfBoundsException e ){
            System.out.println(" ERROR: file name not specified or incorrect number of parameters ");
        }
    }

    private static void create_file(String outputFile) throws IOException {
        List<String> instructions = SymbolTable.instruction_list;
        FileWriter fileWriter = new FileWriter(outputFile);

        for(String line: instructions)
            fileWriter.write(line);
        fileWriter.close();
    }

}
