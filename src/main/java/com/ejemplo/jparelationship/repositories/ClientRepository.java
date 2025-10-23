package com.ejemplo.jparelationship.repositories;

import com.ejemplo.jparelationship.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
