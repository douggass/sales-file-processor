package com.store.fileprocessor.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class FileUtil {

	/**
	 * Method that will know the input paths of files in the system
	 * 
	 * @return the path of the system input directory
	 */
	public String getSystemPathIn() {
		final Properties properties = System.getProperties();
		final String homepath = properties.getProperty("user.home");
		final String pathSeparator = properties.getProperty("file.separator");
		return homepath.concat(pathSeparator).concat("data").concat(pathSeparator).concat("in").concat(pathSeparator);
	}

	/**
	 * Method that will know the output paths of files in the system
	 * 
	 * @return the path of the system output directory
	 */
	public String getSystemPathOut() {
		final Properties properties = System.getProperties();
		final String homepath = properties.getProperty("user.home");
		final String pathSeparator = properties.getProperty("file.separator");
		return homepath.concat(pathSeparator).concat("data").concat(pathSeparator).concat("out").concat(pathSeparator);
	}

	/**
	 * Method that will fetch the files from a directory. Configured as regex: *.txt
	 * and * .csv
	 * 
	 * @param dir - full directory path
	 * @return list of files
	 * @throws IOException
	 */
	public Set<String> listFiles(String dir) throws IOException {
		Set<String> fileList = new HashSet<>();
		Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.toFile().exists() && (file.getFileName().toString().endsWith(".csv")
						|| file.getFileName().toString().endsWith(".txt"))) {
					fileList.add(file.getFileName().toString());
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return fileList;
	}

}
