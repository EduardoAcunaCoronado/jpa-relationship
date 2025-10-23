package com.ejemplo.jparelationship.repositories;

import com.ejemplo.jparelationship.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query("SELECT c FROM Client c join fetch c.addresses")
    Optional<Client> findOne(Long id);

}
