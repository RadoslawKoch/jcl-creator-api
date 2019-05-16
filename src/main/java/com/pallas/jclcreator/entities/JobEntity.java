package com.pallas.jclcreator.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.pallas.jcl.creator.datamodel.interfaces.JobCardDefinition;
import com.pallas.jcl.creator.job.Job;
import com.pallas.jcl.creator.job.exceptions.InvalidNameException;

@Document(collection="jobs")
public class JobEntity 
	extends Job{
	
    @Id
    private String id;
	
    public JobEntity(JobCardDefinition jobCard)throws InvalidNameException {
	super(jobCard);
	this.id = jobCard.getName();
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
        this.id = id;
    }	
}
