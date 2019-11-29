package com.store.fileprocessor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
	private String identifier;
	private String name;
	private String businessArea;
}
