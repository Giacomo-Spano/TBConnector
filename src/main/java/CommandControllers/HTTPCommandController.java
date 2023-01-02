package CommandControllers;


import command.*;
import config.Configuration;
import device.DeviceList;
import importer.Importer;
import kafka.SimpleProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HTTPCommandController extends CommandController {
    private static final Logger LOGGER = LogManager.getLogger(HTTPCommandController.class);

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("/greeting")
    public CommandResult greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        SimpleProducer p = new SimpleProducer();
        p.init();
        p.sendMessage("provatopic", "mesasagigio");
        //p.close();
        return new CommandResult(counter.incrementAndGet(), String.format(template, name));
    }


    @GetMapping("/devices")
    public CommandResult devices(@RequestParam(value = "name", defaultValue = "World") String name) {

        if (Configuration.getImporters() != null && Configuration.getImporters().size() > 0) {
            Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
            while (importerIterator.hasNext()) {
                Importer importer = importerIterator.next();
                DeviceList list = importer.getDevicesList();
                LOGGER.info("list retrived");
            }
        }
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



