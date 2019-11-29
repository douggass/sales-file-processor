package com.store.fileprocessor.mapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.Sale;
import com.store.fileprocessor.dto.SaleDetail;

@Component
public class SaleFieldSetMapper implements FieldSetMapper<FileData> {
	
	private static final String EMPTY = "";
	private static final String DATA_WITHOUT_MINIMUN_INFORMATION = "Data without minimum information";

	/**
	 * Method that will map a sale to a generic object
	 * @param fieldSet	-	field set as configured in batch
	 * @return a generic object with sale
	 * @see {@link FileData}}
	 */
	@Override
	public FileData mapFieldSet(FieldSet fieldSet) throws BindException {
		return FileData.builder().sale(
				Sale.builder()
					.id(fieldSet.readString("id"))
					.details(buildListSaleDetail(fieldSet.readString("data")))
					.salesmanName(fieldSet.readString("salesman_name"))
					.build())
				.build();
	}
	
	/**
	 * Method that will turn a string with all items sold into a list of sales details
	 * @param saleDetail - string of the complete sale that will be transformed
	 * @return sale details list
	 */
	private List<SaleDetail> buildListSaleDetail(final String saleDetail) {
		return Optional.ofNullable(saleDetail)
			.map(str -> str.replace("[", EMPTY).replace("]", EMPTY))
			.map(str -> str.split(","))
			.map(arrayStr -> Arrays.stream(arrayStr)
					.map(this::buildSaleDetail)
					.collect(Collectors.toList()))
			.orElse(Collections.emptyList());
	}

	/**
	 * Method that will transform a sale string into a sale detail object
	 * @param saleDetail - string of a single sale
	 * @return object detail sale {@link SaleDetail}
	 */
	private SaleDetail buildSaleDetail(final String saleDetail) {
		return Optional.ofNullable(saleDetail)
			.map(str -> saleDetail.split("-"))
			.map(arrayStr -> SaleDetail.builder()
					.id(arrayStr[0])
					.price(new BigDecimal(arrayStr[2]))
					.quantity(Long.valueOf(arrayStr[1]))
					.build())
			.orElseThrow(() -> new RuntimeException(DATA_WITHOUT_MINIMUN_INFORMATION));

	}

}
