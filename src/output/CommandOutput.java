package output;

import backend.CurrentSession;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.Database;

public class CommandOutput extends Output {
    public CommandOutput(ArrayNode output, Database database, CurrentSession currentSession) {
        super(output, database, currentSession);
    }


}
