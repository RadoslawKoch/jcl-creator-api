package com.pallas.jclcreator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pallas.jcl.creator.job.steps.params.ExecType;

public class CreateStepDto {

    @NotNull
    @Size(max=8)
    private String name;
	
    @NotNull
    @Size(max=8)
    private String execName;
	
    @NotNull
    private ExecType type;
	
    private String comment;
	
    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public ExecType getType() {
	return type;
    }

    public void setType(ExecType type) {
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getExecName() {
	return execName;
    }

    public void setExecName(String execName) {
	this.execName = execName;
    }	
}
