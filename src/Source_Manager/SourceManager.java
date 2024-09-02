package Source_Manager;
//Author: Juan Dingevan

import java.io.FileNotFoundException;
import java.io.IOException;

public interface SourceManager {
    void open(String filePath) throws FileNotFoundException;

    void close() throws IOException;

    char getNextChar() throws IOException;

    String getPreviousLine();

    int getLineNumber();

    int getColumnNumber();

    boolean isEndOfFile(char character);

    public static final char END_OF_FILE = (char) 26;
}
