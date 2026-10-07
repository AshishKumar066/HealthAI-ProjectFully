package com.healthai.hospital_backend.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
	/** Consultation, Medicine, Lab Test, Room, Emergency, Other */
	private String category;
	private String description;
	private Integer quantity = 1;
	private BigDecimal unitPrice = BigDecimal.ZERO;

	public BigDecimal getLineTotal() {
		return (unitPrice == null ? BigDecimal.ZERO : unitPrice)
				.multiply(BigDecimal.valueOf(quantity == null ? 1 : quantity));
	}
}
