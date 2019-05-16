package com.pallas.jclcreator.dto;

import com.pallas.jclcreator.enums.RoleName;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserDto {
    
    @Email
    @NotNull
    private String email;
    
    @NotNull
    @Size(min=6)
    private String password;
    
    @NotNull
    @Size(min=6)
    private String confirmPassword;
    
    @Size(min=4,max=15)
    private String fname;
    
    @Size(min=4,max=15)
    private String lname;
    
    @NotNull
    private Set<RoleName> roles;

    public Set<RoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleName> roles) {
        this.roles = roles;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
}
