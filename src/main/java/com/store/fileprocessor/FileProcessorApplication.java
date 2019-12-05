package com.store.fileprocessor;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.store.fileprocessor.exception.SetupException;
import com.store.fileprocessor.util.FileUtil;

@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class FileProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessorApplication.class, args);
	}

	@PostConstruct
	private void setup() throws SetupException {
		final String pathData = FileUtil.getSystemPathData();
		final String pathIn = FileUtil.getSystemPathIn();
		final String pathOut = FileUtil.getSystemPathOut();

		File directoryIn = new File(pathIn);
		File directoryOut = new File(pathOut);
		File directoryData = new File(pathData);

		if (!directoryData.exists() && !directoryData.mkdirs()) {
			throw new SetupException("Failed to create data directory");
		}

		if (!directoryIn.exists() && !directoryIn.mkdir()) {
			throw new SetupException("Failed to create directory: ".concat(pathIn));
		}

		if (!directoryOut.exists() && !directoryOut.mkdir()) {
			throw new SetupException("Failed to create directory: ".concat(pathOut));
		}
	}

}
