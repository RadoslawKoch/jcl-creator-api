package com.pallas.jclcreator.service;

import com.pallas.jcl.creator.job.card.params.Programmer;
import com.pallas.jclcreator.builders.UserBuilder;
import com.pallas.jclcreator.dto.ChangePasswordDto;
import com.pallas.jclcreator.dto.CreateUserDto;
import com.pallas.jclcreator.dto.UpdateUserDto;
import com.pallas.jclcreator.dto.UserInfoDto;
import com.pallas.jclcreator.entities.Role;
import com.pallas.jclcreator.entities.User;
import com.pallas.jclcreator.enums.RoleName;
import com.pallas.jclcreator.repos.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class UserService 
    implements UserServiceDefinition{
    
    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encode;
    
    @Autowired
    private TokenStore store;

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        User user = this.repo.findById(string).orElse(null);
        if(null==user){
            throw new UsernameNotFoundException("The user "+string+ " not found!");
        }
        return user;
    }
    
    @Override
    public void add(CreateUserDto dto) throws Exception{
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new Exception("Password doesn't match!");
        }
        this.repo.save(new UserBuilder().setEmail(dto.getEmail()).setPassword(encode.encode(dto.getPassword()))
                .setFname(dto.getFname()).setLname(dto.getLname()).setRoles(dto.getRoles()).build());      
    }
    
    @Override
    public void changePassword(String userId,ChangePasswordDto dto) throws Exception{
        UserDetails user = this.loadUserByUsername(userId);
        if(!encode.matches(dto.getOldPassword(), user.getPassword()))
            throw new Exception("Old password is invalid!");
        if(!dto.getNewPassword().equals(dto.getConfirmNewPassword()))
            throw new Exception("Password dosen't match!");
        User tmp = (User)user;
        tmp.setPassword(encode.encode(dto.getNewPassword()));
        this.repo.save(tmp);    
    }
    
    @Override
    public void logout(){
        final OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder
                .getContext().getAuthentication();
        store.removeAccessToken(store.getAccessToken(auth));
    }
    
    @Override
    public void delete(String userId){
        this.repo.deleteById(userId);
    }
    
    @Override
    public List<UserInfoDto> getAll(){
        List<UserInfoDto> tmp = new ArrayList();
        this.repo.findAll().forEach(x->tmp.add(this.toDto(x)));
        return tmp;
    }
    
    @Override
    public UserInfoDto getUserById(String id){
        User user = this.repo.findById(id).orElse(null);
        if(user==null)
            return null;
        return this.toDto(user);
    }
    
    @Override
    public void updateUser(String userId,UpdateUserDto dto) throws UsernameNotFoundException {
        User user = this.repo.findById(userId).orElse(null);
        if(user==null){
            throw new UsernameNotFoundException("The user "+userId+ " not found!");
        }
        user.setAuthor(new Programmer(dto.getFname(),dto.getLname()));
        Set<Role> tmp = new HashSet();
        dto.getRoles().forEach(x->tmp.add(new Role(x)));
        user.setRoles(tmp);
        this.repo.save(user);
    }
    
    private UserInfoDto toDto(User user){
        UserInfoDto dto = new UserInfoDto();
        dto.setFname(user.getAuthor().getFname());
        dto.setId(user.getId());
        dto.setLname(user.getAuthor().getLname());
        Set<RoleName> tmp = new HashSet();
        user.getRoles().forEach(x->tmp.add(x.getName()));
        dto.setRoles(tmp);
        return dto;
    }
}
