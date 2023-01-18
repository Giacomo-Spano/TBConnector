package CommandReceiver.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class RunScriptCommand extends Command {
    private static final Logger LOGGER = LogManager.getLogger(RunScriptCommand.class);

    public void execute(JSONObject json)  {

        String script = json.getString("script");

        LOGGER.info("execute script " + script);

        //String path="cmd /c start d:\\sample\\sample.bat";
        Runtime rn=Runtime.getRuntime();
        try {
            Process pr=rn.exec(script);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
