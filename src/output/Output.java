package output;

import backend.CurrentSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.Database;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Output {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ArrayNode output;
    private Database database;
    private CurrentSession currentSession;

    public Output(ArrayNode output, Database database, CurrentSession currentSession) {
        this.output = output;
        this.database = database;
        this.currentSession = currentSession;
    }
}
