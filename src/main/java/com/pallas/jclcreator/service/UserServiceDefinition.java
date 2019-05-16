package com.pallas.jclcreator.service;

import com.pallas.jclcreator.dto.ChangePasswordDto;
import com.pallas.jclcreator.dto.CreateUserDto;
import com.pallas.jclcreator.dto.UpdateUserDto;
import com.pallas.jclcreator.dto.UserInfoDto;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceDefinition extends UserDetailsService{
    
    void add(CreateUserDto dto) throws Exception;  
    void changePassword(String userId,ChangePasswordDto dto) throws Exception; 
    void logout();   
    void delete(String userId);   
    List<UserInfoDto> getAll();
    UserInfoDto getUserById(String id);
    void updateUser(String userId,UpdateUserDto dto); 
}
