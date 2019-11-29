package com.store.fileprocessor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileData {
	private Customer customer;
	private Sale sale;
	private Salesman salesman;
}
