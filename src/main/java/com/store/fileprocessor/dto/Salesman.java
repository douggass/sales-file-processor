package com.store.fileprocessor.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Salesman {
	private String type;
	private String identifier;
	private String name;
	private BigDecimal salary;

}
