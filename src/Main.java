import backend.Backend;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Input;

import java.io.File;
import java.io.IOException;

public final class Main {
    private Main() {

    }
    /**
     * Main function - runs the entire program
     * @param args  paths to input and output files
     */
    public static void main(final String[] args) throws IOException {
        // get input
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(args[0]), Input.class);
        // init output node array
        ArrayNode output = objectMapper.createArrayNode();

        Backend backend = new Backend(output, inputData);
        backend.startRunning();

        // write output
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(args[1]), output);
    }
}
