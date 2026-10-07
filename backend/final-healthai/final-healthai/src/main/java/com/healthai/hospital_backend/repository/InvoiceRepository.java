package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	@Query("select i from Invoice i where (:patientId is null or i.patient.id = :patientId) "
			+ "and (:status is null or :status = '' or upper(i.status) = upper(:status)) "
			+ "and (:q is null or :q = '' or lower(i.invoiceNumber) like lower(concat('%', :q, '%')) "
			+ "or lower(i.patient.fullName) like lower(concat('%', :q, '%'))) order by i.id desc")
	List<Invoice> search(@Param("patientId") Long patientId, @Param("status") String status, @Param("q") String q);

	@Query("select coalesce(sum(i.paidAmount), 0) from Invoice i")
	BigDecimal totalRevenue();

	@Query("select coalesce(sum(i.totalAmount - i.paidAmount), 0) from Invoice i where i.status <> 'PAID'")
	BigDecimal totalOutstanding();

	long countByStatusIgnoreCase(String status);
}
