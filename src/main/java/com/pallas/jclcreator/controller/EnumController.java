package com.pallas.jclcreator.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pallas.jcl.creator.job.card.params.KeywordParamName;
import com.pallas.jcl.creator.job.steps.dd.params.DDParamName;
import com.pallas.jcl.creator.job.steps.params.StepParamName;
import com.pallas.jclcreator.enums.RoleName;
import com.pallas.jclcreator.errors.ErrorHandler;

@RestController	
@CrossOrigin
public class EnumController 
	extends ErrorHandler{

    @RequestMapping(method=RequestMethod.GET,value="/keywords")
    public KeywordParamName[] getKeywordParamNames(){
	return KeywordParamName.values();
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/ddparams")
    public DDParamName[] getDDParamNames() {
	return DDParamName.values();
    }
	
    @RequestMapping(method=RequestMethod.GET,value="/stepparams")
    public StepParamName[] getStepParamNames() {
	return StepParamName.values();
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/roles")
    public RoleName[] getRoles(){
        return RoleName.values();
    }
}
