package com.store.fileprocessor.configuration.batch;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import com.store.fileprocessor.business.FileOutDataBusiness;
import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.FileOutData;
import com.store.fileprocessor.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileDataWriter implements ItemWriter<FileData>, Closeable {

	private FileOutDataBusiness fileDataBusiness = new FileOutDataBusiness();

	private final PrintWriter writer;

	@SuppressWarnings("resource")
	public FileDataWriter() {
		OutputStream out;
		try {
			String outPath = FileUtil.getSystemPathOut().concat(UUID.randomUUID().toString().concat(".txt"));
			out = new FileOutputStream(outPath);
		} catch (FileNotFoundException e) {
			log.error("Error: {}", e);
			out = System.out;
		}
		this.writer = new PrintWriter(out);
	}

	@Override
	public void write(final List<? extends FileData> itens) throws Exception {
		FileOutData result = fileDataBusiness.buildFileOutData(itens);
		writer.println(result.toString());
	}

	@PreDestroy
	@Override
	public void close() throws IOException {
		writer.close();
	}
}
