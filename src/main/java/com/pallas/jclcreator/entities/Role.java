package com.pallas.jclcreator.entities;

import com.pallas.jclcreator.enums.RoleName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection="roles")
public class Role 
    implements GrantedAuthority{
    
    @Id
    private RoleName name;

    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name.toString();
    }     
}
