package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String invoiceNumber;
	@ManyToOne(optional = false)
	@JoinColumn(name = "patient_id")
	private Patient patient;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "invoice_items", joinColumns = @JoinColumn(name = "invoice_id"))
	private List<InvoiceItem> items = new ArrayList<>();
	@Column(precision = 12, scale = 2)
	private BigDecimal totalAmount = BigDecimal.ZERO;
	@Column(precision = 12, scale = 2)
	private BigDecimal paidAmount = BigDecimal.ZERO;
	/** PENDING, PARTIAL, PAID */
	private String status = "PENDING";
	private String paymentMethod;
	private LocalDate invoiceDate;

	public BigDecimal getBalance() {
		return totalAmount.subtract(paidAmount);
	}
}
