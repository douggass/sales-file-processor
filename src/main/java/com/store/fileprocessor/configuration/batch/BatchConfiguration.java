package com.store.fileprocessor.configuration.batch;

import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.mapper.CustomerFieldSetMapper;
import com.store.fileprocessor.mapper.SaleFieldSetMapper;
import com.store.fileprocessor.mapper.SalesmanFieldSetMapper;

@Configuration
public class BatchConfiguration {

	private static final String[] CUSTOMER_MAP_NAMES = new String[] { "type", "identifier", "name", "business_area" };
	private static final String[] SALESMAN_MAP_NAMES = new String[] { "type", "identifier", "name", "salary" };
	private static final String[] SALE_MAP_NAMES = new String[] { "type", "id", "data", "salesman_name" };
	private static final String DELIMITER = "ç";

	@Bean
	@JobScope
	public FlatFileItemReader<FileData> reader(@Value("#{jobParameters[fileName]}") String fileName) {
		BeanWrapperFieldSetMapper<FileData> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(FileData.class);

		return new FlatFileItemReaderBuilder<FileData>().name("itemReader").resource(new FileSystemResource(fileName))
				.delimited().names("customer", "sale", "salesman").lineMapper(lineMapper())
				.fieldSetMapper(beanWrapperFieldSetMapper).build();
	}

	/**
	 * Method with read file settings
	 */
	public LineMapper<FileData> lineMapper() {

		HashMap<String, DelimitedLineTokenizer> tokenizers = new HashMap<>();
		tokenizers.put("001*", buildDelimitedLineTokenizer(SALESMAN_MAP_NAMES));
		tokenizers.put("002*", buildDelimitedLineTokenizer(CUSTOMER_MAP_NAMES));
		tokenizers.put("003*", buildDelimitedLineTokenizer(SALE_MAP_NAMES));

		HashMap<String, FieldSetMapper<FileData>> fieldSetMappers = new HashMap<>();
		fieldSetMappers.put("001*", new SalesmanFieldSetMapper());
		fieldSetMappers.put("002*", new CustomerFieldSetMapper());
		fieldSetMappers.put("003*", new SaleFieldSetMapper());

		PatternMatchingCompositeLineMapper patternMatchingCompositeLineMapper = new PatternMatchingCompositeLineMapper();
		patternMatchingCompositeLineMapper.setTokenizers(tokenizers);
		patternMatchingCompositeLineMapper.setFieldSetMappers(fieldSetMappers);

		return patternMatchingCompositeLineMapper;
	}
	
	/**
	 * Method to dynamically tokenize file layout
	 * @param mapNames - definition of the fields that are between the delimiters
	 * @return delimited line tokenizer 
	 * @see {@link DelimitedLineTokenizer}
	 */
	private DelimitedLineTokenizer buildDelimitedLineTokenizer(final String[] mapNames) {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(DELIMITER);
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(mapNames);
		return lineTokenizer;
	}

	/**
	 * Method with processor file settings
	 */
	@Bean
	@JobScope
	public FileDataProcessor processor() {
		return new FileDataProcessor();
	}

	/**
	 * Method with writer file settings
	 */
	@Bean
	@JobScope
	public ItemWriter<FileData> writer() {
		return new FileDataWriter();
	}

	/**
	 * Method with delete file settings
	 * @param fileName - full file path
	 */
	@Bean
	@JobScope
	public Tasklet delete(final @Value("#{jobParameters[fileName]}") String fileName) {
		FileDeletingTasklet delete = new FileDeletingTasklet();
		delete.setFileName(fileName);
		return delete;
	}

	/**
	 * Method that will create a job with your steps
	 */
	@Bean
	public Job importFileDataJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<FileData> reader, ItemProcessor<FileData, FileData> processor, ItemWriter<FileData> writer,
			Tasklet delete) {

		Step stepProcessFile = stepBuilderFactory.get("process_file").<FileData, FileData>chunk(10).reader(reader)
				.processor(processor).writer(writer).build();

		Step stepDeleteIn = stepBuilderFactory.get("delete_file").tasklet(delete).build();

		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).listener(new NotificationListener())
				.start(stepProcessFile).next(stepDeleteIn).build();
	}

}