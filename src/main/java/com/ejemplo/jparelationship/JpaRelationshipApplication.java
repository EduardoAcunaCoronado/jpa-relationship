package com.ejemplo.jparelationship;

import com.ejemplo.jparelationship.entities.Address;
import com.ejemplo.jparelationship.entities.Client;
import com.ejemplo.jparelationship.entities.Invoice;
import com.ejemplo.jparelationship.repositories.ClientRepository;
import com.ejemplo.jparelationship.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
        oneToManyFindById();
    }

    @Transactional
    public void manyToOne() throws Exception {
        System.out.println("Creando un nuevo cliente con nombre 'John' y apellido 'Doe'");
        Client client = new Client(null, "John", "Doe", new ArrayList<>());
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

    @Transactional
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

    public void oneToMany() throws Exception {
        Client client = new Client(null, "Fran", "Moras", new ArrayList<>());

        client.getAddresses().add(new Address(null, "Calle Los Milanos", 6));
        client.getAddresses().add(new Address(null, "Calle Cuartel", 5));

        clientRepository.save(client);

        System.out.println(client);

    }

    public void oneToManyFindById() throws Exception {
        Optional<Client> optionalClient = clientRepository.findById(1L);

        optionalClient.ifPresent(client -> {
            client.setAddresses(Arrays.asList(
                new Address(null, "Calle Los Milanos", 6),
                new Address(null, "Calle Cuartel", 5)
            ));
            Client clientDb = clientRepository.save(client);
            System.out.println(clientDb);
        });

    }


}
