package scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.Job;
import command.WakeUpOnLanCommand;

import static org.quartz.JobBuilder.newJob;

public class WakeUpOnLanCommandJob implements Job {

    private static final Logger LOGGER = LogManager.getLogger(WakeUpOnLanCommandJob.class);

    private static final String TAG = "MagicPacket";

    public static final String BROADCAST = "192.168.1.255";
    public static final int PORT = 9;
    public static final char SEPARATOR = ':';


    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String ipaddress = dataMap.getString("ipaddress");
        String MACAddress = dataMap.getString("macaddress");

        LOGGER.info("execute job " + "MACaddress: " + MACAddress + "ipaddress: " + ipaddress);

        JSONObject json = new JSONObject();
        json.put("ipaddress", ipaddress);
        json.put("MACAddress", MACAddress);

        WakeUpOnLanCommand command = new WakeUpOnLanCommand();
        command.execute(json);
    }
}