package com.store.fileprocessor.business;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.store.fileprocessor.dto.FileData;
import com.store.fileprocessor.dto.FileOutData;
import com.store.fileprocessor.dto.Sale;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class FileOutDataBusiness {
	
	public FileOutData buildFileOutData(final List<? extends FileData> itens) {
		final Long amountCustomers = itens.stream().filter(item -> Objects.nonNull(item.getCustomer())).count();
		final Long amountSellers = itens.stream().filter(item -> Objects.nonNull(item.getSalesman())).count();

		// TODO: what is the rule if there is more than one sale with the same value and
		// both are the most expensive?
		String mostExpensiveSaleId = itens.stream().filter(fileData -> Objects.nonNull(fileData.getSale()))
				.map(FileData::getSale).sorted(Comparator.comparing(Sale::getTotalPrice).reversed()).map(Sale::getId)
				.findFirst().orElse(null);

		Map<String, BigDecimal> salesmanSoldValues = new HashMap<>();
		itens.stream().filter(fileData -> Objects.nonNull(fileData.getSale())).map(FileData::getSale)
				.forEach(sale -> salesmanSoldValues.compute(sale.getSalesmanName(),
						(key, value) -> Optional.ofNullable(value)
								.map(bigDecimalValue -> bigDecimalValue.add(sale.getTotalPrice()))
								.orElse(sale.getTotalPrice())));
		String sellerWithLessSales = salesmanSoldValues.entrySet().stream()
				.min(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(null);

		return FileOutData.builder().amountCustomers(amountCustomers).mostExpensiveSaleId(mostExpensiveSaleId)
				.sellerWithLessSales(sellerWithLessSales).amountSellers(amountSellers).build();
	}

}
