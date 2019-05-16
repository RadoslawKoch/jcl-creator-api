package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;

import com.pallas.jcl.creator.job.card.params.KeywordParamName;

public class CreateKeywordParamDto {

    @NotNull
    private KeywordParamName name;
	
    @NotNull
    private String value;

    public KeywordParamName getName() {
	return name;
    }

    public void setName(KeywordParamName name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }	
}
