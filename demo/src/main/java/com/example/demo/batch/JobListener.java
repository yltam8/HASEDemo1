package com.example.demo.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

    long startTime;
    long endTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        System.out.println(jobExecution.getJobConfigurationName());
        System.out.println("start");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        System.out.println("end");
        System.out.println("Time："+(endTime - startTime) + "ms");
        System.out.println(jobExecution.getStatus());
    }
}