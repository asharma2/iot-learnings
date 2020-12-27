package com.iot.learnings.reverse.geo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.iot.learnings.reverse.geo.exception.ReverseGeoNotFoundException;
import com.iot.learnings.reverse.geo.listener.ReverseGeoCsvDataCompletionListener;
import com.iot.learnings.reverse.geo.model.ReverseGeoCsvData;
import com.iot.learnings.reverse.geo.processor.ReverseGeoCsvDataProcessor;
import com.iot.learnings.reverse.geo.reader.ReverseGeoCsvDataItemReader;
import com.iot.learnings.reverse.geo.writer.ReverseGeoCsvDataItemWriter;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@EnableAsync
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	JobRepository jobRepository;

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadGroupName("RevGeoThreadGroupName");
		executor.setThreadNamePrefix("BatchWorker");
		executor.setMaxPoolSize(4);
		executor.setCorePoolSize(2);
		executor.setQueueCapacity(32);
		return executor;
	}

	@Bean(name = "asyncJobLaucher")
	public JobLauncher asyncJobLaucher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.setTaskExecutor(taskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	@Bean
	@StepScope
	public ReverseGeoCsvDataItemReader reader() {
		return new ReverseGeoCsvDataItemReader();
	}

	@Bean
	@StepScope
	public ReverseGeoCsvDataItemWriter writer() {
		return new ReverseGeoCsvDataItemWriter();
	}

	@Bean
	@StepScope
	public ReverseGeoCsvDataProcessor processor() {
		return new ReverseGeoCsvDataProcessor();
	}

	@Bean
	@Lazy
	public Job revGeoJob(Step revGeoStep) {
		return jobBuilderFactory.get("revGeoJob").incrementer(new RunIdIncrementer()).listener(listener())
				.start(revGeoStep()).build();
	}

	@Bean
	public Step revGeoStep() {
		return stepBuilderFactory.get("revGeoStep").<ReverseGeoCsvData, ReverseGeoCsvData>chunk(1000).reader(reader())
				.processor(processor()).writer(writer()).faultTolerant().skipLimit(10)
				.skip(ReverseGeoNotFoundException.class).build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new ReverseGeoCsvDataCompletionListener();
	}
}
