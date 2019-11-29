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
	
	private List<SaleDetail> buildListSaleDetail(final String saleDetail) {
		return Optional.ofNullable(saleDetail)
			.map(str -> str.replace("[", EMPTY).replace("]", EMPTY))
			.map(str -> str.split(","))
			.map(arrayStr -> Arrays.stream(arrayStr)
					.map(this::buildSaleDetail)
					.collect(Collectors.toList()))
			.orElse(Collections.emptyList());
	}
	
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
