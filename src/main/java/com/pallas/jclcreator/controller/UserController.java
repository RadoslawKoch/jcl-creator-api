package com.pallas.jclcreator.controller;

import com.pallas.jclcreator.dto.ChangePasswordDto;
import com.pallas.jclcreator.dto.CreateUserDto;
import com.pallas.jclcreator.dto.UpdateUserDto;
import com.pallas.jclcreator.dto.UserInfoDto;
import com.pallas.jclcreator.errors.ErrorHandler;
import com.pallas.jclcreator.service.UserServiceDefinition;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController 
    extends ErrorHandler{
    
    @Autowired
    private UserServiceDefinition service;
      
    @RequestMapping(value="/users/logout",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(){
        this.service.logout();
    }
    
    @RequestMapping(value="/users/{id}/password",method=RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('ADMIN') || canEdit(#id)")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable String id,@RequestBody ChangePasswordDto dto) 
            throws Exception{
        this.service.changePassword(id,dto);       
    }
    
    @RequestMapping(value="/users/register",method=RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(
            @RequestBody 
            @Validated 
                    CreateUserDto dto) 
            throws Exception{
        this.service.add(dto);
    }
   
    @RequestMapping(value="/users/{id}",method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(
            @PathVariable String id) 
            throws Exception{
        this.service.delete(id);
    }
    
    @RequestMapping(value="/users/{id}",method=RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('ADMIN') || canEdit(#id)")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(
            @PathVariable String id,
            @RequestBody @Validated UpdateUserDto dto) 
            throws Exception{
        this.service.updateUser(id, dto);
    }
      
    @RequestMapping(value="/users",method=RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserInfoDto> getAllUsers() 
            throws Exception{
        return this.service.getAll();
    }
    
    @RequestMapping(value="/users/{id}",method=RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN') || canEdit(#id)")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto getUserById(@PathVariable String id) 
            throws Exception{
        return this.service.getUserById(id);
    }
}
