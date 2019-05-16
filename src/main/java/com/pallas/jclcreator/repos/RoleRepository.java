package com.pallas.jclcreator.repos;

import com.pallas.jclcreator.entities.Role;
import com.pallas.jclcreator.enums.RoleName;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository 
    extends MongoRepository<Role,String>{
    Role findByName(RoleName name);
}
