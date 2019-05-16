package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateDDStatementDto {

    @NotNull
    @Size(max=8)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
	this.name = name;
    }	
}
