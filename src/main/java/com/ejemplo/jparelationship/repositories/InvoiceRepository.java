package com.ejemplo.jparelationship.repositories;

import com.ejemplo.jparelationship.entities.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
