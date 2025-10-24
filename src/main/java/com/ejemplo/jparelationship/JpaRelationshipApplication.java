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

import java.util.*;

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
        oneToManyInvoiceBidirectionalFindById();
    }

    @Transactional
    public void manyToOne() throws Exception {
        System.out.println("Creando un nuevo cliente con nombre 'John' y apellido 'Doe'");
        Client client = new Client(null, "John", "Doe", new HashSet<>(), new HashSet<>());
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

    @Transactional
    public void oneToMany() throws Exception {
        Client client = new Client(null, "Fran", "Moras", new HashSet<>(), new HashSet<>());

        client.getAddresses().add(new Address(null, "Calle Los Milanos", 6));
        client.getAddresses().add(new Address(null, "Calle Cuartel", 5));

        clientRepository.save(client);

        System.out.println(client);

    }

    @Transactional
    public void oneToManyFindById() throws Exception {
        Optional<Client> optionalClient = clientRepository.findById(1L);

        optionalClient.ifPresent(client -> {
            client.addAddress(new Address(null, "Calle 10", 10));
            client.addAddress(new Address(null, "Calle 11", 11));
            Client clientDb = clientRepository.save(client);
            System.out.println(clientDb);
        });

    }

    @Transactional
    public void removeAddress() throws Exception {
        Client client = new Client(null, "Fran", "Moras", new HashSet<>(), new HashSet<>());

        client.getAddresses().add(new Address(null, "Calle Los Milanos", 6));
        client.getAddresses().add(new Address(null, "Calle Cuartel", 5));

        clientRepository.save(client);

        System.out.println(client);

        Optional<Client> optionalClient = clientRepository.findById(3L);
        optionalClient.ifPresent(c -> {
            c.getAddresses().remove(1);
            Client clientDb = clientRepository.save(c);
            System.out.println(clientDb);
        });

    }

    @Transactional
    public void removeAddressFindById() throws Exception {
        System.out.println("Buscando cliente con ID 2 en la base de datos");
        Optional<Client> optionalClient = clientRepository.findById(2L);

        System.out.println("Verificando si el cliente existe en la base de datos");
        optionalClient.ifPresent(client -> {
            // Si el cliente no tiene direcciones, las creamos
            if (client.getAddresses().isEmpty()) {
                Address address1 = new Address(null, "Calle Los Milanos", 6);
                Address address2 = new Address(null, "Calle Cuartel", 5);
                client.addAddress(address1);
                client.addAddress(address2);
                clientRepository.save(client);
                System.out.println("Direcciones creadas");
                // Ahora buscamos nuevamente el cliente para obtener las direcciones con ID
                Optional<Client> optionalClientDb = clientRepository.findOneWithAddresses(2L);
                optionalClientDb.ifPresent(clientDb -> {
                    System.out.println("Cliente antes de eliminar dirección: " + clientDb);
                    // Eliminamos la dirección por índice (por ejemplo, la segunda dirección)
                    if (clientDb.getAddresses().size() > 1) {
                        clientDb.removeAddress(address2);
                        Client clientDb2 = clientRepository.save(clientDb);
                        System.out.println("Muestro el cliente guardado después de eliminar dirección");
                        System.out.println(clientDb2);
                    }
                });
            }
        });
    }

    @Transactional
    public void oneToManyInvoiceBidirectional() {
        Client client = new Client(null, "Fran", "Moras", new HashSet<>(), new HashSet<>());

        // Crear facturas de forma más compacta
        List<Invoice> invoices = List.of(
                new Invoice(null, "Compra 1", 100.0, client),
                new Invoice(null, "Compra 2", 200.0, client)
        );

        // Añadirlas usando el método helper
        invoices.forEach(client::addInvoice);

        // Guardar el cliente (propagará las facturas si cascade = CascadeType.ALL)
        Client savedClient = clientRepository.save(client);

        // Logs más informativos y ligeros
        System.out.println(savedClient);
    }

    @Transactional
    public void oneToManyInvoiceBidirectionalFindById() {
        Optional<Client> optionalClient = clientRepository.findOneWithInvoices(1L);

        optionalClient.ifPresent(client -> {
            // Crear facturas de forma más compacta
            Set<Invoice> invoices = new HashSet<>();
            invoices.add(new Invoice(null, "Compra 1", 100.0, client));
            invoices.add(new Invoice(null, "Compra 2", 200.0, client));

            // Añadirlas usando el método helper
            client.addInvoice(new Invoice(null, "Compra 1", 100.0, client));
            client.addInvoice(new Invoice(null, "Compra 2", 200.0, client));

            // Guardar el cliente (propagará las facturas si cascade = CascadeType.ALL)
            Client savedClient = clientRepository.save(client);

            // Logs más informativos y ligeros
            System.out.println(savedClient);
        });


    }

}
