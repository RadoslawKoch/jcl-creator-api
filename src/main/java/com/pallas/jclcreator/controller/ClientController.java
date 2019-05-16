package com.pallas.jclcreator.controller;

import com.pallas.jclcreator.dto.ChangePasswordDto;
import com.pallas.jclcreator.dto.ClientInfoDto;
import com.pallas.jclcreator.dto.CreateClientDto;
import com.pallas.jclcreator.dto.UpdateClientDto;
import com.pallas.jclcreator.errors.ErrorHandler;
import com.pallas.jclcreator.service.ClientServiceDefinition;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ClientController 
    extends ErrorHandler {
         
    @Autowired
    private ClientServiceDefinition clientService;
            
    @RequestMapping(value="/clients/register",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createClient(
            @RequestBody 
            @Validated
                    CreateClientDto dto) 
            throws Exception{
        this.clientService.add(dto);
    }
    
    @RequestMapping(value="/clients/{id}",method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(
            @PathVariable String id) 
            throws Exception{
        this.clientService.remove(id);
    }
    
    @RequestMapping(value="/clients",method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ClientInfoDto> getAllClients() 
            throws Exception{
        return this.clientService.getAll();
    }
    
    @RequestMapping(value="/clients/{id}",method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ClientInfoDto getClientById(@PathVariable String id) 
            throws Exception{
        return this.clientService.getById(id);
    }
    
    @RequestMapping(value="/clients/{id}",method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable String id, 
            @RequestBody @Validated UpdateClientDto dto) 
            throws Exception{
        this.clientService.update(id, dto);
    }
    
    @RequestMapping(value="/clients/{id}/password",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeClientSecret(@PathVariable String id, 
            @RequestBody @Validated ChangePasswordDto dto) 
            throws Exception{
        this.clientService.changeSecret(id, dto);
    }
}
