package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateJobDto {

    @NotNull
    @Size(max=8)
    private String name;
		
    @Size(max=12)
    private String accountInfo;
	
    @Size(max=200)
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
	this.name = name;
    }

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
}
