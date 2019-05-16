package com.pallas.jclcreator.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

@Document(collection="clients")
public class Client 
    implements ClientDetails {
    
    @Id
    private String id;
    private String secret;
    private Set<String> resourceIds = new HashSet();
    private boolean secretRequired;
    private boolean scoped;
    private Set<String> scopes = new HashSet();
    private Set<String> authorizedGrantTypes = new HashSet();
    private List<GrantedAuthority> authorites = new ArrayList();
    private Set<String> uri = new HashSet();
    private Map<String, Object> informations = new HashMap();
    private boolean isAutoAprouved;
    private Integer tokenValidity;

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public void setSecretRequired(boolean secretRequired) {
        this.secretRequired = secretRequired;
    }

    public void setScoped(boolean scoped) {
        this.scoped = scoped;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }
  
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public List<GrantedAuthority> getAuthorites() {
        return authorites;
    }

    public void setAuthorites(List<GrantedAuthority> authorites) {
        this.authorites = authorites;
    }

    public Set<String> getUri() {
        return uri;
    }

    public void setUri(Set<String> uri) {
        this.uri = uri;
    }

    public Map<String, Object> getInformations() {
        return informations;
    }

    public void setInformations(Map<String, Object> informations) {
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

    @Override
    public String getClientId() {
        return this.id;
    }

    @Override
    public Set<String> getResourceIds() {
        //This is the list of resources
        //that the current client is able to access
        //The most important think is that one of them
        //has to match the resource Id from 
        //ResourceServerConfigurerAdapter. If it's not the case
        //the client wouldn't be able to access this resource.
//        Set<String> result = new HashSet();
//        result.add("admin_panel");
//        result.add("active_directory");
//        return result;
        return this.resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        //Here we decide if for retrieving the token, the client needs to 
        //pass its secret (password).
        //For security reasons it's a good pratice to make it true, like in our case
        return this.secretRequired;
    }

    @Override
    public String getClientSecret() {
        //returns the user secret (password)
        return this.secret;
    }

    @Override
    public boolean isScoped() {
        //??????????????????????/
        return this.scoped;
    }

    @Override
    public Set<String> getScope() {
        //?????????????????????
        return this.scopes;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        //Very importat elements
        //It allows the client (that normaly is an application
        //to login with one of the crederentials way
        //if we select "client_credentials"
        //The client is able to get the token only with its ID and SECRET. 
        //There is even no need to give any user or its password
        //If we want that the password is required to get the Token
        //We need to allows only the "password" as authorizatedGrantType.
        //This is the case of our exemple:
//        Set<String> result = new HashSet();
//        result.add("password");
//        return result;
        return this.authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        //The URI that user is redirected one the connection to 
        //the application is correctly established.
        return this.uri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        //This is the list of authorities, that the client has
        //and allows him to manipulate the resource server.
        //It is verified at the resource level, and based on that user is
        //allowed or not to get data
        return this.authorites;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.tokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.tokenValidity;
    }

    @Override
    public boolean isAutoApprove(String string) {
        return this.isAutoAprouved;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return this.informations;
    }
    
}
