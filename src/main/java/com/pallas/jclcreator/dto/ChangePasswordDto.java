package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordDto {
    
    @NotNull
    @Size(min=6)
    private String oldPassword;
    
    @NotNull
    @Size(min=6)
    private String newPassword;
    
    @NotNull
    @Size(min=6)
    private String confirmNewPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }  
}
