package com.pallas.jclcreator.builders;

import com.pallas.jclcreator.entities.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ClientBuilder {
    
    private String id;
    private String secret;
    private Set<String> resourceIds = new HashSet();
    private boolean secretRequired;
    private boolean scoped;
    private Set<String> scopes = new HashSet();
    private Set<String> authorizedGrantTypes = new HashSet();
    private final List<GrantedAuthority> authorites = new ArrayList();
    private Set<String> uri = new HashSet();
    private final Map<String, Object> informations = new HashMap();
    private boolean isAutoAprouved;
    private Integer tokenValidity;

    public ClientBuilder setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public ClientBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ClientBuilder setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
        return this;
    }

    public ClientBuilder setSecretRequired(boolean secretRequired) {
        this.secretRequired = secretRequired;
        return this;
    }

    public ClientBuilder setScoped(boolean scoped) {
        this.scoped = scoped;
        return this;
    }

    public ClientBuilder setScopes(Set<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public ClientBuilder setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
        return this;
    }

    public ClientBuilder setAuthorites(Set<String> authorites) {
        authorites.forEach(x->this.authorites.add(new SimpleGrantedAuthority(x)));
        return this;
    }

    public ClientBuilder setUri(Set<String> uri) {
        this.uri = uri;
        return this;
    }

    public ClientBuilder setInformations(Set<String> informations) {
        informations.forEach(x->this.informations.put(x, x));
        return this;
    }

    public ClientBuilder setIsAutoAprouved(boolean isAutoAprouved) {
        this.isAutoAprouved = isAutoAprouved;
        return this;
    }

    public ClientBuilder setTokenValidity(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
        return this;
    }
    
    public Client build(){
        Client tmp = new Client();
        tmp.setAuthorites(authorites);
        tmp.setAuthorizedGrantTypes(authorizedGrantTypes);
        tmp.setInformations(informations);
        tmp.setIsAutoAprouved(isAutoAprouved);
        tmp.setResourceIds(resourceIds);
        tmp.setScoped(scoped);
        tmp.setScopes(scopes);
        tmp.setSecret(secret);
        tmp.setSecretRequired(secretRequired);
        tmp.setTokenValidity(tokenValidity);
        tmp.setUri(uri);
        return tmp;
    }
    
    
    
    
    
    
}
