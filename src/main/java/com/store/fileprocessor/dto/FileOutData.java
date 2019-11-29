package com.store.fileprocessor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileOutData {
	private Long amountSellers;
	private Long amountCustomers;
	private String mostExpensiveSaleId;
	private String sellerWithLessSales;
}
