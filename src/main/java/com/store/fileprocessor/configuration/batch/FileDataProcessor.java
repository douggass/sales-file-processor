package com.store.fileprocessor.configuration.batch;

import org.springframework.batch.item.ItemProcessor;

import com.store.fileprocessor.dto.FileData;

public class FileDataProcessor implements ItemProcessor<FileData, FileData> {

	@Override
	public FileData process(FileData item) throws Exception {
		return item;
	}

}
