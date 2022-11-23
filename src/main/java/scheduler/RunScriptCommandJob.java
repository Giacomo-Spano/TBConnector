package scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.Job;

import java.io.IOException;

import static org.quartz.JobBuilder.newJob;

public class RunScriptCommandJob implements Job {
    private static final Logger LOGGER = LogManager.getLogger(RunScriptCommandJob.class);


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String script = dataMap.getString("script");

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