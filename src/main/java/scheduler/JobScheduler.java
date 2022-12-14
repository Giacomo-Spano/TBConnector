package scheduler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.quartz.JobBuilder.newJob;

public class JobScheduler {

    private static final Logger LOGGER = LogManager.getLogger(JobScheduler.class);

    static Schedule schedule;

    public void init() {

        loadScheduleYAML();

        if (schedule.getJobs().size() == 0)
            return;

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            // and start it off
            scheduler.start();

            Iterator<Job> jobIterator = schedule.getJobs().iterator();
            while (jobIterator.hasNext()) {
                Job j = jobIterator.next();


                if (j.getJobType().equals("mqttcommand")) {
                    JobDetail job = newJob(MQTTCommandJob.class)
                            .withIdentity(j.getName() + "-" + "start", "group1")
                            .usingJobData("topic", j.getTopic())
                            .usingJobData("message", j.getMessage())
                            .build();

                    // 0 0 12 * * ?  Fire at 12pm (noon) every day
                    CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                            .withIdentity(j.getName() + "starttrigger", "group1")
                            .withSchedule(CronScheduleBuilder.cronSchedule(j.getCrontrigger()))
                            //.forJob("myJob", "group1")
                            .build();
                    scheduler.scheduleJob(job, cronTrigger);
                } else if (j.getJobType().equals("runscriptcommand")) {
                    JobDetail job = newJob(RunScriptCommandJob.class)
                            .withIdentity(j.getName() + "-" + "start", "group1")
                            .usingJobData("script", j.getScript())
                            .build();

                    // 0 0 12 * * ?  Fire at 12pm (noon) every day
                    CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                            .withIdentity(j.getName() + "starttrigger", "group1")
                            .withSchedule(CronScheduleBuilder.cronSchedule(j.getCrontrigger()))
                            //.forJob("myJob", "group1")
                            .build();
                    scheduler.scheduleJob(job, cronTrigger);
                } else if (j.getJobType().equals("wolcommand")) {
                    JobDetail job = newJob(WakeUpOnLanCommandJob.class)
                            .withIdentity(j.getName() + "-" + "start", "group1")
                            .usingJobData("macaddress", j.getMacaddress())
                            .usingJobData("ipaddress", j.getIpaddress())

                            .build();

                    // 0 0 12 * * ?  Fire at 12pm (noon) every day

                    CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                            .withIdentity(j.getName() + "starttrigger", "group1")
                            .withSchedule(CronScheduleBuilder.cronSchedule(j.getCrontrigger()))
                            //.forJob("myJob", "group1")
                            .build();
                    scheduler.scheduleJob(job, cronTrigger);
                }
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
