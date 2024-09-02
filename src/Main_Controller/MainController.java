package Main_Controller;

import Lexical_Analyzer.*;
import Source_Manager.SourceManager;
import Source_Manager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainController {

    public static void main( String[] args ){
        try{
            String inputFile = args[0];

            if( args.length > 2 ){
                System.out.println("Incorrect number of parameters");
            }else{
                try{

                    SourceManager sourceManager = new SourceManagerImpl();
                    sourceManager.open(inputFile);
                    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceManager);

                    System.out.println("Lexical analysis completed succesfully");

                }catch ( FileNotFoundException e ){
                    System.out.println("ERROR: file not found");
                }catch ( IOException e ){
                    e.printStackTrace();
                } catch ( LexicalException e ){
                    System.out.println( e.getMessage() );
                }

            }
        }catch( ArrayIndexOutOfBoundsException e ){
            System.out.println(" ERROR: file name not specified or incorrect number of parameters ");
        }
    }

}
