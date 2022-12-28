package device;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Shelly1 extends Shelly {
    private static final Logger LOGGER = LogManager.getLogger(Shelly1.class);

    public Shelly1(Device device) {
        super(device);
    }

    public Shelly1(JSONObject json) {
        super(json);
    }

}
