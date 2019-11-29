package com.store.fileprocessor.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleDetail {

	private String id;
	private Long quantity;
	private BigDecimal price;

}
