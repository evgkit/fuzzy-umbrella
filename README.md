# Indeed Jobs

It uses data from the job aggregator [Indeed](http://indeed.com).
Current data is cached and included in the repository.

### To refresh the job data
Sign up to be an [Indeed publisher](https://www.indeed.com/publisher).  Add your publisher key to the [config.properties](src/main/resources/config.properties) file,
and set `shouldRefresh` in [App.java](src/main/java/ru/evgkit/jobs/App.java#L13).