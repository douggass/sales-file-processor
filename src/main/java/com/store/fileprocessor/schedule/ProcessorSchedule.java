package com.store.fileprocessor.schedule;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
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

	@Autowired
	private FileUtil fileUtil;

	private boolean running = false;
	
	private final static Pattern p = Pattern.compile("[*.csv][*.txt]");
	

	@Scheduled(fixedRate = 5000)
	public void run() throws IOException {
		if (!running) {
			running = true;
			Set<String> listFiles = listFiles(fileUtil.getSystemPathIn());

			log.info("list files: {}", listFiles);

			if (!listFiles.isEmpty()) {
				listFiles.forEach(item -> {
					final JobParameters jobParameter = new JobParametersBuilder()
							.addLong("time", System.currentTimeMillis())
							.addString("fileName", fileUtil.getSystemPathIn().concat(item)).toJobParameters();

					JobExecution execution;
					try {
						execution = jobLauncher.run(job, jobParameter);
						log.info("Exit status: {}", execution.getStatus());
					} catch (Exception e) {
						log.error("Error: {}", e);
					}
				});
			}
			running = false;
		}

	}

	public Set<String> listFiles(String dir) throws IOException {
		Set<String> fileList = new HashSet<>();
		Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Matcher m = p.matcher(file.getFileName().toString());
				if (file.toFile().exists() && m.find()) {
					fileList.add(file.getFileName().toString());
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return fileList;
	}

}
