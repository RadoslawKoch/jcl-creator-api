package com.pallas.jclcreator.dto;

import com.pallas.jclcreator.enums.RoleName;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserDto {
    
    @NotNull
    @Size(min=1,max=12)
    private String fname;
    
    @NotNull
    @Size(min=1,max=12)
    private String lname;
    
    @NotNull
    private Set<RoleName> roles;

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
