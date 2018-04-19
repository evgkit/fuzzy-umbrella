package ru.evgkit.jobs;

import ru.evgkit.jobs.model.Job;
import ru.evgkit.jobs.service.JobService;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        displayCompaniesMenuUsingRange(getCompanies(jobs));
    }

    private static List<String> getCompanies(List<Job> jobs) {
        return jobs.stream()
                .map(Job::getCompany)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private static void displayCompaniesMenuUsingRange(List<String> companies) {
        int pageSize = 20;
        IntStream.iterate(1, i -> i + pageSize)
            .mapToObj(i -> String.format("%d. %s", i, companies.get(i - 1)))
            .limit(companies.size() / pageSize)
            .forEach(System.out::println);
    }

    private static void displayCompaniesMenuImperatively(List<String> companies) {
        for (int i = 0; i < 20; i++) {
            System.out.printf("%d. %s %n", i + 1, companies.get(i));
        }
    }

    // Optional
    private static void luckySearchJob(List<Job> jobs, String searchTerm) {
        jobs.stream()
            .filter(job -> job.getTitle().contains(searchTerm))
            .findFirst()
            .ifPresent(job -> System.out.println(job.getTitle()));
    }

    // Reduction
    private static Optional<String> getLargestCompanyName(List<Job> jobs) {
        return jobs.stream()
            .map(Job::getCompany)
            .max(Comparator.comparingInt(String::length));
    }

    // Streams
    private static void printJobsImperatively(List<Job> jobs) {
        for (Job job : jobs) {
            if (job.getState().equals("OR") && job.getCity().equals("Portland")) {
                System.out.println(job);
            }
        }
    }

    private static void printJobsStream(List<Job> jobs) {
        jobs.stream()
            .filter(
                job -> job.getState().equals("OR") && job.getCity().equals("Portland")
            )
            .forEach(System.out::println);
    }

    // Collectors
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

    private static List<Job> getThreeJunJobsStream(List<Job> jobs) {
        return jobs.stream()
            .filter(App::isJuniorJob)
            .limit(3)
            .collect(Collectors.toList());
    }

    // Mapping
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

    private static List<String> getCaptionsStream(List<Job> jobs) {
        return jobs.parallelStream()
            .filter(App::isJuniorJob)
            .map(Job::getCaption)
            .limit(3)
            .collect(Collectors.toList());
    }

    private static boolean isJuniorJob(Job job) {
        String title = job.getTitle().toLowerCase();
        return title.contains("junior") || title.contains("jr");
    }

    private static Map<String, Long> getSnippetWordCountsImperatively(List<Job> jobs) {
        Map<String, Long> wordCounts = new HashMap<>();

        for (Job job : jobs) {
            String[] words = job.getSnippet().split("\\W+");
            for (String word : words) {
                if (word.length() == 0) {
                    continue;
                }
                String lWord = word.toLowerCase();
                Long count = wordCounts.get(lWord);
                if (count == null) {
                    count = 0L;
                }
                wordCounts.put(lWord, ++count);
            }
        }
        return wordCounts;
    }

    private static Map<String, Long> getSnippetWordCountsStream(List<Job> jobs) {
        return jobs.stream()
            .map(Job::getSnippet)
            .map(snippet -> snippet.split("\\W+"))
            .flatMap(Stream::of)
            .filter(word -> word.length() > 0)
            .map(String::toLowerCase)
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
    }
}
