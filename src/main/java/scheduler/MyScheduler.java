package scheduler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.quartz.JobBuilder.newJob;

public class MyScheduler {

    static Schedule schedule;

    public void init() {

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            // and start it off
            scheduler.start();

            loadScheduleYAML();

            Iterator<Job> jobIterator = schedule.getJobs().iterator();
            while (jobIterator.hasNext()) {
                Job j = jobIterator.next();

                // schedule start
                JobDetail startjob = newJob(SimpleJob.class)
                        .withIdentity(j.getName() + "-" + "start", "group1")
                        .usingJobData("topic", j.getTopic())
                        .usingJobData("type", j.getType())
                        .usingJobData("command", "start")
                        .build();
                CronTrigger startCronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(j.getName() + "starttrigger", "group1")
                        .withSchedule(CronScheduleBuilder.cronSchedule(j.getStart()))
                        //.forJob("myJob", "group1")
                        .build();
                //scheduler.scheduleJob(startjob, startCronTrigger);

                // schedule shutdown
                JobDetail shutdownjob = newJob(SimpleJob.class)
                        .withIdentity(j.getName() + "-" + "shutdown", "group1")
                        .usingJobData("topic", j.getTopic())
                        .usingJobData("type", j.getType())
                        .usingJobData("command", "shutdown")
                        .build();
                CronTrigger shutdownCronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(j.getName() + "shutdowntrigger", "group1")
                        .withSchedule(CronScheduleBuilder.cronSchedule(j.getShutdown()))
                        //.forJob("myJob", "group1")
                        .build();

                scheduler.scheduleJob(startjob, startCronTrigger);
                scheduler.scheduleJob(shutdownjob, shutdownCronTrigger);

            }

             //scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();

        }
    }

    public  void loadScheduleYAML()  {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // configuring the objectMapper to ignore the error in case we have some
        // unrecognized fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        mapper.findAndRegisterModules();
        try {
            schedule = mapper.readValue(new File("./config/schedule.yaml"), Schedule.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
