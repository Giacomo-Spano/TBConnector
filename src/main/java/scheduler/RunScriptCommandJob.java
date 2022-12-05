package scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.Job;
import command.RunScriptCommand;

import static org.quartz.JobBuilder.newJob;

public class RunScriptCommandJob implements Job {
    private static final Logger LOGGER = LogManager.getLogger(RunScriptCommandJob.class);


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String script = dataMap.getString("script");

        LOGGER.info("execute script " + script);

        JSONObject json = new JSONObject();
        json.put("script", script);

        RunScriptCommand command = new RunScriptCommand();
        command.execute(json);
    }
}