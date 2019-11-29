package com.store.fileprocessor.mapper;

import java.math.BigDecimal;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.Salesman;

@Component
public class SalesmanFieldSetMapper implements FieldSetMapper<FileData> {

	@Override
	public FileData mapFieldSet(FieldSet fieldSet) throws BindException {
		return FileData.builder()
				.salesman(Salesman.builder().type(fieldSet.readString("type"))
						.identifier(fieldSet.readString("identifier"))

						.salary(new BigDecimal(fieldSet.readString("salary").replace(",", "")))

						.name(fieldSet.readString("name")).build())
				.build();
	}

}
