package ru.evgkit.jobs;

import com.sun.org.apache.xpath.internal.SourceTree;
import ru.evgkit.jobs.model.Job;
import ru.evgkit.jobs.service.JobService;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) {
        JobService service = new JobService();
        boolean shouldRefresh = false;
        try {
            if (shouldRefresh) {
                service.refresh();
            }
            List<Job> jobs = service.loadJobs();
            System.out.printf("Total jobs:  %d %n %n", jobs.size());
            explore(jobs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void explore(List<Job> jobs) {
        printJobsStream(jobs);
    }

    private static void printJobsStream(List<Job> jobs) {
        jobs.stream()
            .filter(
                job -> job.getState().equals("OR") && job.getCity().equals("Portland")
            )
            .forEach(System.out::println);
    }

    private static void printJobsImperatively(List<Job> jobs) {
        for (Job job : jobs) {
            if (job.getState().equals("OR") && job.getCity().equals("Portland")) {
                System.out.println(job);
            }
        }
    }
}
