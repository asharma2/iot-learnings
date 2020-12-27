package com.iot.learnings.reverse.geo.reader;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import com.iot.learnings.reverse.geo.model.ReverseGeoCsvData;

public class ReverseGeoCsvDataItemReader implements StepExecutionListener, ItemReader<ReverseGeoCsvData> {

	private String revGeoFile;
	private FlatFileItemReader<ReverseGeoCsvData> itemReader;
	private ExecutionContext executionContext;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.revGeoFile = stepExecution.getJobExecution().getJobParameters().getString("revGeoFile");
		this.executionContext = stepExecution.getExecutionContext();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		itemReader.close();
		return ExitStatus.COMPLETED;
	}

	@Override
	public ReverseGeoCsvData read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (itemReader == null) {
			itemReader = new FlatFileItemReader<ReverseGeoCsvData>();
			itemReader.setResource(new ClassPathResource(revGeoFile));
			itemReader.setLineMapper(new DefaultLineMapper<ReverseGeoCsvData>() {
				{
					setLineTokenizer(new DelimitedLineTokenizer() {
						{
							setNames(new String[] { "city", "lat", "lon", "country", "iso2", "iso3", "population" });
						}
					});
					setFieldSetMapper(new BeanWrapperFieldSetMapper<ReverseGeoCsvData>() {
						{
							setTargetType(ReverseGeoCsvData.class);
						}
					});
				}
			});
			itemReader.setLinesToSkip(1);
			itemReader.open(executionContext);
		}
		return itemReader.read();
	}
}
