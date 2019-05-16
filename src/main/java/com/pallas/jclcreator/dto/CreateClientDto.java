package com.pallas.jclcreator.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateClientDto {
    
    @NotNull
    @Size(min=4,max=15)
    private String clientId;
    
    @Size(min=4,max=25)
    private String secret;
    
    @Size(min=4,max=25)
    private String confirmSecret;
    
    private Set<String> resourceIds = new HashSet();
    private boolean secretRequired;
    private boolean scoped;
    private Set<String> scopes = new HashSet();
    private Set<String> authorizedGrantTypes = new HashSet();
    private Set<String> authorites = new HashSet();
    private Set<String> uri = new HashSet();
    private Set<String> informations = new HashSet();
    private boolean isAutoAprouved;
    private Integer tokenValidity;

    public String getClientId() {
        return clientId;
    }

    public String getConfirmSecret() {
        return confirmSecret;
    }

    public void setConfirmSecret(String confirmSecret) {
        this.confirmSecret = confirmSecret;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public boolean isSecretRequired() {
        return secretRequired;
    }

    public void setSecretRequired(boolean secretRequired) {
        this.secretRequired = secretRequired;
    }

    public boolean isScoped() {
        return scoped;
    }

    public void setScoped(boolean scoped) {
        this.scoped = scoped;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public Set<String> getAuthorites() {
        return authorites;
    }

    public void setAuthorites(Set<String> authorites) {
        this.authorites = authorites;
    }

    public Set<String> getUri() {
        return uri;
    }

    public void setUri(Set<String> uri) {
        this.uri = uri;
    }

    public Set<String> getInformations() {
        return informations;
    }

    public void setInformations(Set<String> informations) {
        this.informations = informations;
    }

    public boolean isIsAutoAprouved() {
        return isAutoAprouved;
    }

    public void setIsAutoAprouved(boolean isAutoAprouved) {
        this.isAutoAprouved = isAutoAprouved;
    }

    public Integer getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
    }     
}
