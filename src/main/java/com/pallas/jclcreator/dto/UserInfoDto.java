package com.pallas.jclcreator.dto;

import com.pallas.jclcreator.enums.RoleName;
import java.util.Set;

public class UserInfoDto {
    
    private String id;
    private String fname;
    private String lname;
    private Set<RoleName> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Set<RoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleName> roles) {
        this.roles = roles;
    }  
}
