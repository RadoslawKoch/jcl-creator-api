package com.pallas.jclcreator.builders;

import com.pallas.jcl.creator.job.card.params.Programmer;
import com.pallas.jclcreator.entities.Role;
import com.pallas.jclcreator.entities.User;
import com.pallas.jclcreator.enums.RoleName;
import java.util.HashSet;
import java.util.Set;

public class UserBuilder {
    
    private String email;
    private String password;
    private String fname;
    private String lname;
    private final Set<Role> roles = new HashSet();

    public UserBuilder setRoles(Set<RoleName> roles) {
        roles.forEach(x->this.roles.add(new Role(x)));
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setFname(String fname) {
        this.fname = fname;
        return this;
    }

    public UserBuilder setLname(String lname) {
        this.lname = lname;
        return this;
    }
    
    public User build(){
        Programmer pgm = new Programmer(this.fname,this.lname);
        return new User(this.email,pgm,this.password,this.roles);    
    }   
}
