package com.store.fileprocessor.business;

import java.util.List;

import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.FileOutData;

public interface FileOutDataBusiness {

	public FileOutData buildFileOutData(final List<? extends FileData> itens);

}
