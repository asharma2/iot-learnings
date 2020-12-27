package com.iot.learnings.reverse.geo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, QuartzAutoConfiguration.class })
public class ReverseGeoApp implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ReverseGeoApp.class);

	@Autowired
	JobLauncher asyncJobLaucher;

	@Autowired
	BeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(ReverseGeoApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("Going to run one time job started");
		Job job = beanFactory.getBean("revGeoJob", Job.class);
		JobParameters jobParameters = new JobParametersBuilder().addString("revGeoFile", "reversegeo.csv")
				.addDate("runAt", new Date()).toJobParameters();
		JobExecution execution = asyncJobLaucher.run(job, jobParameters);
		LOG.info("Going to run one time job completed. Status: {}", execution.getStatus());
	}
}
