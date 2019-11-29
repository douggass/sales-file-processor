package com.store.fileprocessor.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.store.fileprocessor.dto.Customer;
import com.store.fileprocessor.dto.FileData;

@Component
public class CustomerFieldSetMapper implements FieldSetMapper<FileData> {
	
	/**
	 * Method that will map a customer to a generic object
	 * @param fieldSet	-	field set as configured in batch
	 * @return a generic object with customer
	 * @see {@link FileData}}
	 */
	@Override
	public FileData mapFieldSet(FieldSet fieldSet) throws BindException {
		return FileData.builder()
				.customer(Customer.builder()
						.identifier(fieldSet.readString("identifier")).name(fieldSet.readString("name"))
						.businessArea(fieldSet.readString("business_area")).build())
				.build();
	}

}
