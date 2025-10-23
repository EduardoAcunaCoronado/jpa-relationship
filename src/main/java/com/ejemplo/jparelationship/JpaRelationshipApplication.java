package com.ejemplo.jparelationship;

import com.ejemplo.jparelationship.entities.Client;
import com.ejemplo.jparelationship.entities.Invoice;
import com.ejemplo.jparelationship.repositories.ClientRepository;
import com.ejemplo.jparelationship.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

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
        manyToOneFindById();
    }

    public void manyToOne() throws Exception {
        System.out.println("Creando un nuevo cliente con nombre 'John' y apellido 'Doe'");
        Client client = new Client(null, "John", "Doe");
        System.out.println("Guardando el cliente en la base de datos");
        clientRepository.save(client);

        System.out.println("Creando una nueva factura con descripción 'Compra' y total 1.5");
        Invoice invoice = new Invoice(null, "Compra", 1.5, null);
        System.out.println("Asociando el cliente a la factura");
        invoice.setClient(client);
        System.out.println("Guardando la factura en la base de datos");
        Invoice invoiceDb = invoiceRepository.save(invoice);
        System.out.println("Mostrando la factura guardada:");
        System.out.println(invoiceDb);

    }

    public void manyToOneFindById() throws Exception {
        System.out.println("Buscando cliente con ID 1 en la base de datos");
        Optional<Client> optionalClient = clientRepository.findById(1L);

        System.out.println("Verificando si el cliente existe en la base de datos");
        if(optionalClient.isPresent()) {
            System.out.println("Obteniendo el cliente del Optional");
            Client client = optionalClient.orElseThrow();
            System.out.println("Creando una nueva factura con descripción 'Compra' y total 1.5");
            Invoice invoice = new Invoice(null, "Compra", 1.5, null);
            System.out.println("Asociando el cliente encontrado a la factura");
            invoice.setClient(client);
            System.out.println("Guardando la factura en la base de datos");
            Invoice invoiceDb = invoiceRepository.save(invoice);
            System.out.println("Mostrando la factura guardada:");
            System.out.println(invoiceDb);
        }
    }

    public void manyToMany() throws Exception {

    }
}
