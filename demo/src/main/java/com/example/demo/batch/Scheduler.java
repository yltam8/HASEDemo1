package com.example.demo.batch;

import java.io.File;
import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;
    
    private boolean enabled = false;
    
	@Value("${batch.target.name}")
	private String target;

    @Scheduled(fixedRate=3000)
	void execute() {
    	if (enabled){
			try {
				JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
			            .addString("type", "Transaction")
			            .toJobParameters();
		        jobLauncher.run(job, jobParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
    	}
    }
    
    public static void renameFile(File toBeRenamed, String new_name) {
		//need to be in the same path
		File fileWithNewName = new File(toBeRenamed.getParent(), new_name);
		if (fileWithNewName.exists()) {
			System.out.println("Target can not be renamed 1");
		}
		// Rename file (or directory)
		boolean success = toBeRenamed.renameTo(fileWithNewName);
		if (!success) {
		    System.out.println("Target can not be renamed");
		}
    }

    public String on() {
    	enabled = true;
		return "OK";
    }
    
    public String off() {
    	enabled = false;
		return "OK";
    }
	
}