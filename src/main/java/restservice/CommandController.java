package restservice;


import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import command.Command;
import command.MQTTCommand;
import command.RunScriptCommand;
import command.WakeUpOnLanCommand;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CommandController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public CommandResult greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new CommandResult(counter.incrementAndGet(), String.format(template, name));
    }

    @PostMapping(value = "/command", consumes = "application/json", produces = "application/json")
    public CommandResult sendcommand(@RequestBody CommandJSON commandJSON) {

        JSONObject result = new JSONObject();

        String command = commandJSON.getCommand();
        if (command.equals("MQTT")) {
            Command cmd = new MQTTCommand();
            cmd.execute(commandJSON.getJSON());
        } else if (command.equals("runscript")) {
            Command cmd = new RunScriptCommand();
            cmd.execute(commandJSON.getJSON());
        } else if (command.equals("wol")) {
            Command cmd = new WakeUpOnLanCommand();
            cmd.execute(commandJSON.getJSON());
        } else {
            CommandResult returnValue = new CommandResult(1,"failure");
            return returnValue;
        }

        CommandResult returnValue = new CommandResult(1,"success");
        return returnValue;
    }
}



