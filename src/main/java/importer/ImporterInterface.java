package importer;

import org.json.JSONObject;

public interface ImporterInterface {
    String name2 = "";
    default void sendCommand(String deviceid, String command, JSONObject param) {

    };
}
