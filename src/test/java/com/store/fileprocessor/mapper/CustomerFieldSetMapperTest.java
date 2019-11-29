package com.store.fileprocessor.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;

import com.store.fileprocessor.dto.FileData;

@SpringBootTest
class CustomerFieldSetMapperTest {

	@InjectMocks
	private CustomerFieldSetMapper customerFieldSetMapper;

	private static final String IDENTIFIER = "32145437000100";
	private static final String NAME = "Roberto S S";
	private static final String BUSINESS_AREA = "Rural";

	@Test
	void mapFieldSetTest() throws BindException {

		String[] names = new String[] { "identifier", "name", "business_area" };
		String[] tokens = new String[] { IDENTIFIER, NAME, BUSINESS_AREA };
		FieldSet fieldSet = new DefaultFieldSet(tokens, names);

		FileData fileData = customerFieldSetMapper.mapFieldSet(fieldSet);

		Assertions.assertThat(fileData.getCustomer()).isNotNull();

		Assertions.assertThat(fileData.getCustomer().getIdentifier()).isEqualTo(IDENTIFIER);
		Assertions.assertThat(fileData.getCustomer().getName()).isEqualTo(NAME);
		Assertions.assertThat(fileData.getCustomer().getBusinessArea()).isEqualTo(BUSINESS_AREA);
	}

}
