package com.store.fileprocessor.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {

	private static final String HOME_PATH = System.getProperties().getProperty("user.home");
	private static final String PATH_SEPARATOR = System.getProperties().getProperty("file.separator");
	private static final String DATA = "data";
	private static final String OUT = "out";
	private static final String IN = "in";
	private static final String TXT = ".txt";
	private static final String CSV = ".csv";

	private FileUtil() {
	}

	/**
	 * Method that will know the input paths of files in the system
	 * 
	 * @return the path of the system input directory
	 */
	public static String getSystemPathIn() {
		return HOME_PATH.concat(PATH_SEPARATOR).concat(DATA).concat(PATH_SEPARATOR).concat(IN).concat(PATH_SEPARATOR);
	}

	/**
	 * Method that will know the output paths of files in the system
	 * 
	 * @return the path of the system output directory
	 */
	public static String getSystemPathOut() {
		return HOME_PATH.concat(PATH_SEPARATOR).concat(DATA).concat(PATH_SEPARATOR).concat(OUT).concat(PATH_SEPARATOR);
	}

	/**
	 * Method that will know the data directory
	 * 
	 * @return the path of the system data directory
	 */
	public static String getSystemPathData() {
		return HOME_PATH.concat(PATH_SEPARATOR).concat(DATA).concat(PATH_SEPARATOR);
	}

	/**
	 * Method that will fetch the files from a directory. Configured as regex: *.txt
	 * and * .csv
	 * 
	 * @param dir - full directory path
	 * @return list of files
	 * @throws IOException
	 */
	public static Set<String> listFiles(String dir) throws IOException {
		Set<String> fileList = new HashSet<>();
		Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.toFile().exists() && (file.getFileName().toString().endsWith(CSV)
						|| file.getFileName().toString().endsWith(TXT))) {
					fileList.add(file.getFileName().toString());
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return fileList;
	}

}
