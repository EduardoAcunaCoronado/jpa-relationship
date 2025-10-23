package com.ejemplo.jparelationship.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "description", "total", "client"})
@EqualsAndHashCode(of = "id")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Double total;

    @ManyToOne
    //@JoinColumn(name = "id_cliente_temp")
    private Client client;

}
