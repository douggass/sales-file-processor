package com.store.fileprocessor.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.store.fileprocessor.dto.Customer;
import com.store.fileprocessor.dto.FileData;

@Component
public class CustomerFieldSetMapper implements FieldSetMapper<FileData> {

	@Override
	public FileData mapFieldSet(FieldSet fieldSet) throws BindException {
		return FileData.builder()
				.customer(Customer.builder().type(fieldSet.readString("type"))
						.identifier(fieldSet.readString("identifier")).name(fieldSet.readString("name"))
						.businessArea(fieldSet.readString("business_area")).build())
				.build();
	}

}
