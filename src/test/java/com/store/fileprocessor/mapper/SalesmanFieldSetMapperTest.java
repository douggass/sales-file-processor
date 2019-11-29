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
class SalesmanFieldSetMapperTest {

	@InjectMocks
	private SalesmanFieldSetMapper salesmanFieldSetMapper;

	private static final String IDENTIFIER = "32145437000100";
	private static final String NAME = "Roberto S S";
	private static final String SALARY = "4000";

	@Test
	void mapFieldSetTest() throws BindException {

		String[] names = new String[] { "identifier", "name", "salary" };
		String[] tokens = new String[] { IDENTIFIER, NAME, SALARY };
		FieldSet fieldSet = new DefaultFieldSet(tokens, names);

		FileData fileData = salesmanFieldSetMapper.mapFieldSet(fieldSet);

		Assertions.assertThat(fileData.getSalesman()).isNotNull();

		Assertions.assertThat(fileData.getSalesman().getIdentifier()).isEqualTo(IDENTIFIER);
		Assertions.assertThat(fileData.getSalesman().getName()).isEqualTo(NAME);
		Assertions.assertThat(fileData.getSalesman().getSalary()).isEqualTo(SALARY);
	}

}
