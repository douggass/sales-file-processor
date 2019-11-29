package com.store.fileprocessor.configuration.batch;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import lombok.Setter;

public class FileDeletingTasklet implements Tasklet, InitializingBean {

	@Setter
	private String fileName;

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		if (!Files.deleteIfExists(Paths.get(fileName))) {
			throw new UnexpectedJobExecutionException("Could not delete file: ".concat(fileName));
		}
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(fileName, "directory must be set");
	}
}
