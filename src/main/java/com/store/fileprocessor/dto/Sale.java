package com.store.fileprocessor.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sale {
	private String id;
	private List<SaleDetail> details;
	private String salesmanName;
	
	/**
	 * Method to perform the sum of the sale
	 * @return a sum of the sale totalizer
	 */
	public BigDecimal getTotalPrice() {
		return Optional.ofNullable(this.details).orElse(Collections.emptyList()).stream()
				.map(detail -> detail.getPrice().multiply(new BigDecimal(String.valueOf(detail.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
