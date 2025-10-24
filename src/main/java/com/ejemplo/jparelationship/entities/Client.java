package com.ejemplo.jparelationship.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_clientes_to_direcciones",
        joinColumns = @JoinColumn(name = "id_cliente"),
        inverseJoinColumns = @JoinColumn(name = "id_direcciones"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_direcciones"})
    )
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invoice> invoices = new HashSet<>();

    // Métodos de conveniencia
    public void addAddress(Address address) {
        addresses.add(address);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        invoice.setClient(this); // 🔥 Mantiene ambos lados sincronizados
    }

    public void removeInvoice(Invoice invoice) {
        invoices.remove(invoice);
        invoice.setClient(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client{id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", lastname='").append(lastname).append('\'');

        // Direcciones
        if (addresses != null && !addresses.isEmpty()) {
            sb.append(", addresses=[");
            addresses.forEach(addr -> sb.append(addr.toString()).append(", "));
            sb.setLength(sb.length() - 2); // quitar última coma
            sb.append("]");
        } else {
            sb.append(", addresses=[]");
        }

        // Facturas (solo mostramos descripción y total)
        if (invoices != null && !invoices.isEmpty()) {
            sb.append(", invoices=[");
            invoices.forEach(inv ->
                    sb.append("{desc='").append(inv.getDescription())
                            .append("', total=").append(inv.getTotal()).append("}, "));
            sb.setLength(sb.length() - 2);
            sb.append("]");
        } else {
            sb.append(", invoices=[]");
        }

        sb.append('}');
        return sb.toString();
    }



}
