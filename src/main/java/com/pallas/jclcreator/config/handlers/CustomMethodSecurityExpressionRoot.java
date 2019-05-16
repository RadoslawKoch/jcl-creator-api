package com.pallas.jclcreator.config.handlers;

import com.pallas.jclcreator.entities.User;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot
    extends SecurityExpressionRoot 
    implements MethodSecurityExpressionOperations {
    
    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean canEdit(String id) {
        String tmp = ((User) this.getPrincipal()).getId();
        return tmp.equals(id);
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }
 
}
