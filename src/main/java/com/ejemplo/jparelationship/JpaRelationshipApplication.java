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
import java.util.List;
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
        oneToManyInvoiceBidirectional();
    }

    @Transactional
    public void manyToOne() throws Exception {
        System.out.println("Creando un nuevo cliente con nombre 'John' y apellido 'Doe'");
        Client client = new Client(null, "John", "Doe", new ArrayList<>(), new ArrayList<>());
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
        Client client = new Client(null, "Fran", "Moras", new ArrayList<>(), new ArrayList<>());

        client.getAddresses().add(new Address(null, "Calle Los Milanos", 6));
        client.getAddresses().add(new Address(null, "Calle Cuartel", 5));

        clientRepository.save(client);

        System.out.println(client);

    }

    @Transactional
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

    @Transactional
    public void removeAddress() throws Exception {
        Client client = new Client(null, "Fran", "Moras", new ArrayList<>(), new ArrayList<>());

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
                client.setAddresses(Arrays.asList(address1, address2));
                clientRepository.save(client);
                System.out.println("Direcciones creadas");
                // Ahora buscamos nuevamente el cliente para obtener las direcciones con ID
                Optional<Client> optionalClientDb = clientRepository.findOne(2L);
                optionalClientDb.ifPresent(clientDb -> {
                    System.out.println("Cliente antes de eliminar dirección: " + clientDb);
                    // Eliminamos la dirección por índice (por ejemplo, la segunda dirección)
                    if (clientDb.getAddresses().size() > 1) {
                        clientDb.removeAddress(clientDb.getAddresses().get(1));
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
        Client client = new Client();
        client.setName("Fran");
        client.setLastname("Moras");

        Invoice invoice1 = new Invoice();
        invoice1.setDescription("Compra 1");
        invoice1.setTotal(100.0);

        Invoice invoice2 = new Invoice();
        invoice2.setDescription("Compra 2");
        invoice2.setTotal(200.0);

        // Usamos los métodos convenientes
        client.addInvoice(invoice1);
        client.addInvoice(invoice2);

        System.out.println(client);

        Client clientDb = clientRepository.save(client);

        System.out.println("✅ Cliente y facturas guardados correctamente");
        System.out.println(clientDb);
    }



}
