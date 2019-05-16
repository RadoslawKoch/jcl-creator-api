package com.pallas.jclcreator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServer 
    extends ResourceServerConfigurerAdapter{   
    
    //PLEASE DONT USE .hasRole method. There is a bug and it doesn't work!
    //the method to be used is .hasAnyAuthority("ADMIN");
    @Override
    public void configure(HttpSecurity http) throws Exception {
         http
                .authorizeRequests()
                 .anyRequest()
                 .authenticated();
//                 .antMatchers("/register","/register/client").permitAll()
//                 .antMatchers("/jobs/**").hasAnyAuthority("ADMIN");
    }

    

    @Override//Here we configure the parameters of our Resource Security Server
    //The most important part is to set a resourceId that has to be included
    //into our Client. 
    //ATTENTION 
    //PLEASE DONT INCLUDE HERE TOKEN STORE AND AUTH MANAGER
    //It provokes error that is No AuthenticationProvider found
    //The Resource server is using the settings (tokenStore and AuthenticationManager)
    //from AuthenticationServer. The only one thing that we must provide here
    //is the resourceId!!!!!!!
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources//.tokenStore(store).authenticationManager(manager)
                .resourceId("admin_panel");
    }
    
    
    
    
    
    
}
