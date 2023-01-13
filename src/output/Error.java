package output;

import backend.CurrentSession;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.Database;

import java.util.ArrayList;

public class Error extends  Output {

    public Error(ArrayNode output, Database database, CurrentSession currentSession) {
        super(output, database, currentSession);
    }

    // TODO: REDO OUTPUT - SUCCESS AND ERROR STRUCTURE and COMMANDS
    // TODO: IMPLEMENT USERS WITH HASHTABLE
    public void genericError() {
        ObjectNode objectNode = getObjectMapper().createObjectNode();

        objectNode.put("error", "Error");
        objectNode.putPOJO("currentMoviesList", new ArrayList<>());
        objectNode.putPOJO("currentUser", null);

        getOutput().addPOJO(objectNode);
    }
}
