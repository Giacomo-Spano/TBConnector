package CommandReceiver.command;


import org.json.JSONObject;

public class CommandJSON {
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        if (command != null)
            json.put("deviceid", deviceid);
        if (command != null)
            json.put("command", command);
        if (command != null)
            json.put("topic", topic);
        if (command != null)
            json.put("message", message);
        if (command != null)
            json.put("script", script);
        if (command != null)
            json.put("ipaddress", ipaddress);
        if (command != null)
            json.put("macaddress", macaddress);
        return json;
    }
    public String getCommand() {
        return command;
    }
    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public String getScript() {
        return script;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public String getMacaddress() {
        return macaddress;
    }

    private final String command;
    private final String deviceid;
    private final String topic;
    private final String message;
    private final String script;
    private final String ipaddress;
    private final String macaddress;

    public CommandJSON(String command, String deviceid, String topic, String message, String script, String ipaddress, String macaddress) {
        this.command = command;
        this.deviceid = deviceid;
        this.topic = topic;
        this.message = message;
        this.script = script;
        this.ipaddress = ipaddress;
        this.macaddress = macaddress;
    }
}
