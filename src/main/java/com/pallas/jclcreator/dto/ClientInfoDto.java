package com.pallas.jclcreator.dto;

import java.util.HashSet;
import java.util.Set;

public class ClientInfoDto {
    
    private String clientId;
    private Set<String> resourceIds = new HashSet();

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }  
}
