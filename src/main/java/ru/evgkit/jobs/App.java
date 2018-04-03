package ru.evgkit.jobs;

import com.sun.org.apache.xpath.internal.SourceTree;
import ru.evgkit.jobs.model.Job;
import ru.evgkit.jobs.service.JobService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private static boolean isJuniorJob(Job job) {
        String title = job.getTitle().toLowerCase();
        return title.contains("junior") || title.contains("jr");
    }

    private static void explore(List<Job> jobs) {
        getThreeJunJobsStream(jobs).forEach(System.out::println);
    }

    private static List<Job> getThreeJunJobsStream(List<Job> jobs) {
        return jobs.stream()
            .filter(App::isJuniorJob)
            .limit(3)
            .collect(Collectors.toList());
    }

    private static List<String> getCaptionsStream(List<Job> jobs) {
        return jobs.parallelStream()
            .filter(App::isJuniorJob)
            .map(Job::getCaption)
            .limit(3)
            .collect(Collectors.toList());
    }

    private static void printJobsStream(List<Job> jobs) {
        jobs.stream()
            .filter(
                job -> job.getState().equals("OR") && job.getCity().equals("Portland")
            )
            .forEach(System.out::println);
    }

    private static List<Job> getThreeJunJobsImperatively(List<Job> jobs) {
        List<Job> juniorJobs = new ArrayList<>();
        for (Job job : jobs) {
            if (isJuniorJob(job)) {
                juniorJobs.add(job);
                if (juniorJobs.size() >= 3) {
                    break;
                }
            }
        }
        return juniorJobs;
    }

    private static List<String> getCaptionsImperatively(List<Job> jobs) {
        List<String> captions = new ArrayList<>();
        for (Job job : jobs) {
            if (isJuniorJob(job)) {
                captions.add(job.getCaption());
                if (captions.size() >= 3) {
                    break;
                }
            }
        }
        return captions;
    }

    private static void printJobsImperatively(List<Job> jobs) {
        for (Job job : jobs) {
            if (job.getState().equals("OR") && job.getCity().equals("Portland")) {
                System.out.println(job);
            }
        }
    }
}
