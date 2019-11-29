package com.store.fileprocessor.util;

import java.util.Properties;

import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class FileUtil {
	
	public String getSystemPathIn() {
		final Properties properties = System.getProperties();
		final String homepath = properties.getProperty("user.home");
		final String pathSeparator = properties.getProperty("file.separator");
		return homepath.concat(pathSeparator).concat("data").concat(pathSeparator).concat("in").concat(pathSeparator);
	}

	public String getSystemPathOut() {
		final Properties properties = System.getProperties();
		final String homepath = properties.getProperty("user.home");
		final String pathSeparator = properties.getProperty("file.separator");
		return homepath.concat(pathSeparator).concat("data").concat(pathSeparator).concat("out").concat(pathSeparator);
	}

}
