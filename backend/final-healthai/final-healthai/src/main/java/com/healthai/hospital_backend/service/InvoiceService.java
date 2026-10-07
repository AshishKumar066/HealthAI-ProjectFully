package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.Invoice;
import com.healthai.hospital_backend.entity.InvoiceItem;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
	private final InvoiceRepository repo;
	private final PatientService patients;
	private final NotificationService notifications;

	public InvoiceService(InvoiceRepository repo, PatientService patients, NotificationService notifications) {
		this.repo = repo;
		this.patients = patients;
		this.notifications = notifications;
	}

	public List<Invoice> search(Long patientId, String status, String q) {
		return repo.search(patientId, status, q);
	}

	public Invoice get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice", id));
	}

	@Transactional
	public Invoice create(InvoiceReq r) {
		Invoice inv = new Invoice();
		inv.setPatient(patients.get(r.patientId()));
		inv.setInvoiceDate(LocalDate.now());
		setItems(inv, r.items());
		BigDecimal paid = r.paidAmount() == null ? BigDecimal.ZERO : r.paidAmount();
		if (paid.compareTo(inv.getTotalAmount()) > 0)
			throw new BadRequestException("Paid amount cannot exceed the total");
		inv.setPaidAmount(paid);
		inv.setPaymentMethod(r.paymentMethod());
		refreshStatus(inv);
		inv = repo.save(inv);
		inv.setInvoiceNumber(String.format("INV-%s-%04d", LocalDate.now().getYear(), inv.getId()));
		inv = repo.save(inv);
		notifications.push("BILLING", "Invoice created",
				inv.getInvoiceNumber() + " for " + inv.getPatient().getFullName() + " - total " + inv.getTotalAmount());
		return inv;
	}

	@Transactional
	public Invoice update(Long id, InvoiceReq r) {
		Invoice inv = get(id);
		inv.setPatient(patients.get(r.patientId()));
		setItems(inv, r.items());
		if (r.paidAmount() != null)
			inv.setPaidAmount(r.paidAmount());
		if (inv.getPaidAmount().compareTo(inv.getTotalAmount()) > 0)
			throw new BadRequestException("Paid amount cannot exceed the total");
		if (r.paymentMethod() != null)
			inv.setPaymentMethod(r.paymentMethod());
		refreshStatus(inv);
		return repo.save(inv);
	}

	@Transactional
	public Invoice pay(Long id, PaymentReq p) {
		Invoice inv = get(id);
		if (p.amount().compareTo(inv.getBalance()) > 0)
			throw new BadRequestException("Payment exceeds the outstanding balance of " + inv.getBalance());
		inv.setPaidAmount(inv.getPaidAmount().add(p.amount()));
		if (p.method() != null)
			inv.setPaymentMethod(p.method());
		refreshStatus(inv);
		Invoice saved = repo.save(inv);
		notifications.push("BILLING", "Payment received",
				saved.getInvoiceNumber() + ": " + p.amount() + " received (" + saved.getStatus() + ")");
		return saved;
	}

	public void delete(Long id) {
		repo.delete(get(id));
	}

	private void setItems(Invoice inv, List<ItemReq> items) {
		List<InvoiceItem> list = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;
		for (ItemReq i : items) {
			InvoiceItem it = new InvoiceItem(i.category(), i.description(), i.quantity() == null ? 1 : i.quantity(),
					i.unitPrice());
			total = total.add(it.getLineTotal());
			list.add(it);
		}
		inv.getItems().clear();
		inv.getItems().addAll(list);
		inv.setTotalAmount(total);
	}

	private void refreshStatus(Invoice inv) {
		if (inv.getPaidAmount().compareTo(inv.getTotalAmount()) >= 0 && inv.getTotalAmount().signum() > 0)
			inv.setStatus("PAID");
		else if (inv.getPaidAmount().signum() > 0)
			inv.setStatus("PARTIAL");
		else
			inv.setStatus("PENDING");
	}
}
