package com.iot.learnings.reverse.geo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class ReverseGeoCsvDataCompletionListener extends JobExecutionListenerSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ReverseGeoCsvDataCompletionListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		BatchStatus batchStatus = jobExecution.getStatus();
		LOG.info("Status: {}", batchStatus);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOG.info("Batch job completed successfully");
		}
	}
}
