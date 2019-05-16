package com.pallas.jclcreator.config;

import com.pallas.jclcreator.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer 
    extends AuthorizationServerConfigurerAdapter{
    
    
    //USE THE BEAN FROM WebSecurityConfigurationAdapter
    @Autowired
    private AuthenticationManager authenticationManager;
    
    //@Autowired
    //private UserService repo;//UserService is introuduced in AuthenticationManager(SecurityConfiguration)
    
   // @Autowired//Service that contains all the operations you can perform on tokens. 
    //ATTENTION! You can use the default TokenService and inject only your token store;
    //THIS IS THE BEST PRATICE, you dont have to care about it and you can store 
    //your token wherever you want
   // private TokenService token;
    
    @Autowired//You can add your personalize token (if you expose a proper bean)| By default its the InMemoryTokenStore that is used
    private TokenStore store;
    
    
    //Client service that contains informations about Clients that are able to use this authorization server
    @Autowired
    private ClientService client;

    
    //THIS CONFIGURATION IS OBLIGATORY
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //Setting authentication manager that is used by the framework. Normally, its the same for both side, application and server and is created
        //on the resource side in WebSecurityConfigurerAdapter
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(store);//etting tokenStore that you want to personalize (can be used with default TokenService
                //.tokenServices(token);//If you have a personalize TokenService, you can inject it here. If not, the default service is used
        //that in most of cases is suffisient.
    }

    
    
    //Create configuration of ClientDetailsServivce in AuthorizationServer.
    //Attention! Here we need to implement our propeer ClientDetailsService
    //if we want to have the whole control on what's going on with
    //In this application, the class ClientService implements the logic.
    //In this method we set this class to be use for all client requests.
    //If we want only use that for testing purpose, we can use clients.inMemory() options.
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(client);
//                .inMemory()
//                .withClient("main-client")
//                .authorizedGrantTypes("client-credentials","password")
//                .scopes("read","write","trust")
//                .resourceIds("oauth2-resource")
//                .accessTokenValiditySeconds(50000)
//                .secret("secret");
        
    }

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//	security.tokenKeyAccess("hasAuthority('ROLE_READER')");
//    }
//    
  
    
}
