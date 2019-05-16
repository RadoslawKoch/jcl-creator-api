package com.pallas.jclcreator.service;

import com.pallas.jclcreator.dto.ChangePasswordDto;
import com.pallas.jclcreator.dto.ClientInfoDto;
import com.pallas.jclcreator.dto.CreateClientDto;
import com.pallas.jclcreator.dto.UpdateClientDto;
import java.util.List;
import org.springframework.security.oauth2.provider.ClientDetailsService;


public interface ClientServiceDefinition 
    extends ClientDetailsService{
    
    void add(CreateClientDto dto) throws Exception;
    void update(String clientId,UpdateClientDto dto) throws Exception;  
    void changeSecret(String clientId,ChangePasswordDto dto) throws Exception;
    void remove(String id);  
    List<ClientInfoDto> getAll();  
    ClientInfoDto getById(String id);   
}
