package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;

import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;

public class CreateDDParamDto {

    @NotNull
    private DDParamName name;
	
    @NotNull
    private String value;

    public DDParamName getName() {
	return name;
    }

    public void setName(DDParamName name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }	
}
