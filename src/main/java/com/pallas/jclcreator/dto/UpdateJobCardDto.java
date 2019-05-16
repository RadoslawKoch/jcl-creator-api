package com.pallas.jclcreator.dto;

import javax.validation.constraints.Size;

public class UpdateJobCardDto {

    @Size(max=15)
    private String accountInfo;
	
    @Size(max=200)
    private String comment;
	
    @Size(max=15)
    private String lastName;

    public String getAccountInfo() {
	return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
	this.accountInfo = accountInfo;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }	
}
