package com.store.fileprocessor.mapper;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;

import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.SaleDetail;

@SpringBootTest
class SaleFieldSetMapperTest {

	@InjectMocks
	private SaleFieldSetMapper saleFieldSetMapper;

	private static final String ID = "123";

	private static final Long QUANTITY_1 = 2L;
	private static final Long QUANTITY_2 = 20L;
	private static final Long QUANTITY_7 = 9L;

	private static final BigDecimal PRICE_1 = new BigDecimal("32");
	private static final BigDecimal PRICE_2 = new BigDecimal("90");
	private static final BigDecimal PRICE_7 = new BigDecimal("0.432");

	private static final String DATA_1 = "1-".concat(QUANTITY_1.toString()).concat("-").concat(PRICE_1.toString());
	private static final String DATA_2 = "2-".concat(QUANTITY_2.toString()).concat("-").concat(PRICE_2.toString());
	private static final String DATA_7 = "7-".concat(QUANTITY_7.toString()).concat("-").concat(PRICE_7.toString());

	private static final String DATA = "[".concat(DATA_1).concat(",").concat(DATA_2).concat(",").concat(DATA_7)
			.concat("]");

	private static final String SALESMAN_NAME = "Amaral N M";

	@Test
	void mapFieldSetTest() throws BindException {

		String[] names = new String[] { "id", "data", "salesman_name" };
		String[] tokens = new String[] { ID, DATA, SALESMAN_NAME };
		FieldSet fieldSet = new DefaultFieldSet(tokens, names);

		FileData fileData = saleFieldSetMapper.mapFieldSet(fieldSet);

		Assertions.assertThat(fileData.getSale()).isNotNull();

		Assertions.assertThat(fileData.getSale().getId()).isEqualTo(ID);
		Assertions.assertThat(fileData.getSale().getSalesmanName()).isEqualTo(SALESMAN_NAME);

		Assertions.assertThat(fileData.getSale().getDetails()).isNotEmpty();

		Assertions.assertThat(fileData.getSale().getDetails()).contains(
				SaleDetail.builder().id("1").price(PRICE_1).quantity(QUANTITY_1).build(),
				SaleDetail.builder().id("2").price(PRICE_2).quantity(QUANTITY_2).build(),
				SaleDetail.builder().id("7").price(PRICE_7).quantity(QUANTITY_7).build());

		BigDecimal totalPrice = PRICE_1.multiply(new BigDecimal(QUANTITY_1.toString()))
				.add(PRICE_2.multiply(new BigDecimal(QUANTITY_2.toString())))
				.add(PRICE_7.multiply(new BigDecimal(QUANTITY_7.toString())));

		Assertions.assertThat(fileData.getSale().getTotalPrice()).isEqualTo(totalPrice);
	}

}
