package device;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class LaneAgentDevice extends Device {
    private static final Logger LOGGER = LogManager.getLogger(LaneAgentDevice.class);
    LocalDateTime lastUpdate;

    public LaneAgentDevice(Device device) {
        super(device);


        CHECKPeriodicStatusUpdate();

    }
    public void receiveMessage(String command, LocalDateTime localDateTime, String topic, String message) {

        LOGGER.info("receive message" + topic + ", " + message);

        lastUpdate = LocalDateTime.now();
        publishStatusMessage(localDateTime,1.0);
    }

    public void CHECKPeriodicStatusUpdate() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                LOGGER.info("Task CHECKPeriodicStatusUpdate() performed on " + new Date());
                LocalDateTime now = LocalDateTime.now();
                long diff = ChronoUnit.SECONDS.between(lastUpdate,now);
                if (diff > 60) {
                    LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
                    publishStatusMessage(localDateTime,0.0);
                }
            }
        };

        LocalDateTime now = LocalDateTime.now();
        lastUpdate = now.plusSeconds(-61);

        Timer timer = new Timer("Timer");
        long delay = 1000L;
        long period = 1000L * 30L; // every 30 seconds
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }
}
