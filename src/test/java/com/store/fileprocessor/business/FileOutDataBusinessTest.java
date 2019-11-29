package com.store.fileprocessor.business;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.fileprocessor.dto.Customer;
import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.FileOutData;
import com.store.fileprocessor.dto.Sale;
import com.store.fileprocessor.dto.SaleDetail;
import com.store.fileprocessor.dto.Salesman;

@SpringBootTest
class FileOutDataBusinessTest {

	@InjectMocks
	private FileOutDataBusiness fileOutDataBusiness;

	private static final String BUSINESS_AREA = "rural";
	private static final String IDENTIFIER = "92385361000111";
	private static final String NAME = "Joao Ltda";

	private static final String OUTHER_BUSINESS_AREA = "hospitalar";
	private static final String OUTHER_IDENTIFIER = "92385361000111";
	private static final String OUTHER_NAME = "Joao Ltda";

	private static final BigDecimal SALESMAN_SALARY = new BigDecimal("500000");
	private static final String SALESMAN_IDENTIFIER = "92385361000111";
	private static final String SALESMAN_NAME = "Joao S S";
	
	private static final String OUTHER_SALESMAN_NAME = "Jose S S";

	private static final String SALE_ID = "32";
	private static final String ITEM_ID = "123";
	private static final Long ITEM_QUANTITY = 100L;
	private static final BigDecimal ITEM_PRICE = new BigDecimal("600");

	private static final String OUTHER_SALE_ID_2 = "12313131";
	private static final String OUTHER_ITEM_ID = "123";
	private static final Long OUTHER_ITEM_QUANTITY = 100L;
	private static final BigDecimal OUTHER_ITEM_PRICE = new BigDecimal("600");

	private static final String OUTHER_ITEM_ID_2 = "123";
	private static final Long OUTHER_ITEM_QUANTITY_2 = 100L;
	private static final BigDecimal OUTHER_ITEM_PRICE_2 = new BigDecimal("600");
	
	
	
	@Test
	void buildFileOutDataTest() {
		
		List<FileData> customerList = buildCustomerListFileData();
		List<FileData> salesmanList = buildSalesmanListFileData();
		List<FileData> saleList = buildSaleListFileData();
		
		List<FileData> itens = Stream.of(customerList, salesmanList, saleList)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		
		
		FileOutData fileOutData = fileOutDataBusiness.buildFileOutData(itens);
		
		Assertions.assertThat(fileOutData.getAmountCustomers()).isEqualTo(customerList.size());
		Assertions.assertThat(fileOutData.getAmountSellers()).isEqualTo(salesmanList.size());
		
		//TODO: refactor: no use constants
		Assertions.assertThat(fileOutData.getMostExpensiveSaleId()).isEqualTo(OUTHER_SALE_ID_2);
		Assertions.assertThat(fileOutData.getSellerWithLessSales()).isEqualTo(SALESMAN_NAME);

	}

	private List<FileData> buildCustomerListFileData() {
		return Arrays.asList(
				FileData.builder()
				.customer(Customer.builder()
						.businessArea(BUSINESS_AREA)
						.identifier(IDENTIFIER)
						.name(NAME)
						.build())
				.build(),
				FileData.builder()
				.customer(Customer.builder()
						.businessArea(OUTHER_BUSINESS_AREA)
						.identifier(OUTHER_IDENTIFIER)
						.name(OUTHER_NAME)
						.build())
				.build()
		);
	}

	private List<FileData> buildSalesmanListFileData() {
		return Arrays.asList(
				FileData.builder()
					.salesman(Salesman.builder()
							.name(SALESMAN_NAME)
							.salary(SALESMAN_SALARY)
							.identifier(SALESMAN_IDENTIFIER)
							.build())
				.build()
				);
	}
	
	private List<FileData> buildSaleListFileData() {
		return Arrays.asList(
				FileData.builder()
				.sale(Sale.builder()
						.id(SALE_ID)
						.salesmanName(SALESMAN_NAME)
						.details(Arrays.asList(
								SaleDetail.builder()
									.quantity(ITEM_QUANTITY)
									.price(ITEM_PRICE)
									.id(ITEM_ID)
								.build()
						))
						.build())
				.build(),
				FileData.builder()
				.sale(Sale.builder()
						.id(OUTHER_SALE_ID_2)
						.salesmanName(OUTHER_SALESMAN_NAME)
						.details(Arrays.asList(
								SaleDetail.builder()
									.quantity(OUTHER_ITEM_QUANTITY)
									.price(OUTHER_ITEM_PRICE)
									.id(OUTHER_ITEM_ID)
									.build(),
								SaleDetail.builder()
									.quantity(OUTHER_ITEM_QUANTITY_2)
									.price(OUTHER_ITEM_PRICE_2)
									.id(OUTHER_ITEM_ID_2)
									.build()
								))
						.build())
				.build()
		);
				
	}

}
