package com.pallas.jclcreator.repos;

import com.pallas.jclcreator.entities.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository 
    extends MongoRepository<Client,String>{}
