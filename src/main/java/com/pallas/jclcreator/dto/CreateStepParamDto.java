package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;


import com.pallas.jcl.creator.job.steps.params.StepParamName;

public class CreateStepParamDto {

    @NotNull
    private StepParamName name;
	
    @NotNull
    private String value;

    public StepParamName getName() {
	return name;
    }

    public void setName(StepParamName name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }	
}
