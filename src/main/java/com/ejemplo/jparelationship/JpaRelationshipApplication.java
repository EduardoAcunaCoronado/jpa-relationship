package com.ejemplo.jparelationship;

import com.ejemplo.jparelationship.entities.Client;
import com.ejemplo.jparelationship.entities.Invoice;
import com.ejemplo.jparelationship.repositories.ClientRepository;
import com.ejemplo.jparelationship.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaRelationshipApplication implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaRelationshipApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        manyToOne();
    }

    public void manyToOne() throws Exception {
        Client client = new Client(null, "John", "Doe");
        clientRepository.save(client);

        Invoice invoice = new Invoice(null, "Compra", 1.5, null);
        invoice.setClient(client);
        Invoice invoiceDb = invoiceRepository.save(invoice);
        System.out.println(invoiceDb);

    }

    public void manyToMany() throws Exception {

    }
}
