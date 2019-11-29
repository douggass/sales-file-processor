package com.store.fileprocessor;

import java.io.File;
import java.util.Properties;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class FileProcessorApplication {

	public static void main(String[] args) {
		final Properties properties = System.getProperties();
		final String homepath = properties.getProperty("user.home");
		final String pathSeparator = properties.getProperty("file.separator");

		final String pathData = homepath.concat(pathSeparator).concat("data");
		final String pathIn = homepath.concat(pathSeparator).concat("data").concat(pathSeparator).concat("in");
		final String pathOut = homepath.concat(pathSeparator).concat("data").concat(pathSeparator).concat("out");

		File directoryIn = new File(pathIn);
		File directoryOut = new File(pathOut);
		File directoryData = new File(pathData);

		if (!directoryData.exists() && !directoryData.mkdirs()) {
			throw new RuntimeException("Failed to create data directory");
		}

		if (!directoryIn.exists() && !directoryIn.mkdir()) {
			throw new RuntimeException("Failed to create directory: ".concat(pathIn));
		}

		if (!directoryOut.exists() && !directoryOut.mkdir()) {
			throw new RuntimeException("Failed to create directory: ".concat(pathOut));
		}
		SpringApplication.run(FileProcessorApplication.class, args);
	}

}
