package com.store.fileprocessor.schedule;

import java.io.IOException;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.store.fileprocessor.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcessorSchedule {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	private boolean running = false;

	/**
	 * Method that will control the system over the rules: From time to time it will
	 * fetch the files in an input directory, perform processing on the file, and
	 * then create a new file processed in an output directory
	 * 
	 * @throws IOException
	 */
	@Scheduled(fixedDelayString = "${system.configurations.rateTime}")
	public void run() throws IOException {
		if (!running) {
			running = true;
			Set<String> listFiles = FileUtil.listFiles(FileUtil.getSystemPathIn());

			log.info("list files: {}", listFiles);

			if (!listFiles.isEmpty()) {
				listFiles.forEach(item -> {
					final JobParameters jobParameter = new JobParametersBuilder()
							.addLong("time", System.currentTimeMillis())
							.addString("fileName", FileUtil.getSystemPathIn().concat(item)).toJobParameters();
					try {
						JobExecution execution = jobLauncher.run(job, jobParameter);
						log.info("Exit status: {} , steps: {}", execution.getStatus(), execution.getStepExecutions());
					} catch (JobExecutionAlreadyRunningException | JobRestartException
							| JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
						log.error("Error: {}", e);
					}

				});
			}
			running = false;
		}
	}

}
