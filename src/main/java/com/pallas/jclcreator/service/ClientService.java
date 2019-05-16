package com.pallas.jclcreator.service;

import com.pallas.jclcreator.builders.ClientBuilder;
import com.pallas.jclcreator.dto.ChangePasswordDto;
import com.pallas.jclcreator.dto.ClientInfoDto;
import com.pallas.jclcreator.dto.CreateClientDto;
import com.pallas.jclcreator.dto.UpdateClientDto;
import com.pallas.jclcreator.entities.Client;
import com.pallas.jclcreator.repos.ClientRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ClientService 
    implements ClientServiceDefinition {
    
    @Autowired
    private ClientRepository repo;
    
    @Autowired
    private PasswordEncoder encode;

    @Override
    public ClientDetails loadClientByClientId(String string) throws ClientRegistrationException {
        ClientDetails client = this.repo.findById(string).orElse(null);
        if(client==null){
            throw new ClientRegistrationException("Client not found!");
        }   
        return client;
    }
    
    @Override
    public void add(CreateClientDto dto) throws Exception{
        if(!dto.getSecret().equals(dto.getConfirmSecret())){
            throw new Exception("Password dosen't match!");
        }
        this.repo.save(new ClientBuilder().
        setAuthorites(dto.getAuthorites()).
        setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes()).
        setInformations(dto.getInformations()).
        setSecret(encode.encode(dto.getSecret())).
        setResourceIds(dto.getResourceIds()).
        setScoped(dto.isScoped()).
        setSecret(dto.getSecret()).
        setSecretRequired(dto.isSecretRequired()).
        setTokenValidity(dto.getTokenValidity()).
        setUri(dto.getUri()).
        setIsAutoAprouved(dto.isIsAutoAprouved()).
        setId(dto.getClientId()).build());
    } 
    
    @Override
    public void update(String clientId,UpdateClientDto dto) throws Exception{
        Client client = this.repo.findById(clientId).orElse(null);
        if(client==null){
            throw new ClientRegistrationException("Client not found!");
        }  
        List<GrantedAuthority> tmp = new ArrayList();
        dto.getAuthorites().forEach(x->tmp.add(new SimpleGrantedAuthority(x)));
        client.setAuthorites(tmp);
        client.setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes());
        Map<String,Object> t = new HashMap();
        dto.getInformations().forEach(x->t.put(t.toString(), t));
        client.setInformations(t);
        client.setIsAutoAprouved(dto.isIsAutoAprouved());
        client.setResourceIds(dto.getResourceIds());
        client.setScoped(dto.isScoped());
        client.setScopes(dto.getScopes());
        client.setSecretRequired(dto.isSecretRequired());
        client.setTokenValidity(dto.getTokenValidity());
        client.setUri(dto.getUri());
        this.repo.save(client);
    } 
    
    @Override
    public void changeSecret(String clientId,ChangePasswordDto dto) throws Exception{
        ClientDetails client = loadClientByClientId(clientId);
        if(!encode.matches(dto.getOldPassword(), client.getClientSecret())){
            throw new Exception("Old password is invalid!");
        }
        if(!dto.getNewPassword().equals(dto.getConfirmNewPassword())){
            throw new Exception("Passords dosen't match!");
        }
        Client tmp = (Client)client;
        tmp.setSecret(encode.encode(dto.getNewPassword()));
        this.repo.save(tmp);     
    }
    
    @Override
    public void remove(String id){
        this.repo.deleteById(id);
    }
    
    @Override
    public List<ClientInfoDto> getAll(){
        List<ClientInfoDto> list = new ArrayList();
        this.repo.findAll().forEach(x->list.add(toDto(x)));
        return list;
    }
    
    @Override
    public ClientInfoDto getById(String id){
        Client tmp = this.repo.findById(id).orElse(null);
        if(tmp==null)
            return null;
        return toDto(tmp);
    }
    
    private ClientInfoDto toDto(Client client){
        ClientInfoDto dto = new ClientInfoDto();
        dto.setClientId(client.getClientId());
        dto.setResourceIds(client.getResourceIds());
        return dto;
    }  
}
