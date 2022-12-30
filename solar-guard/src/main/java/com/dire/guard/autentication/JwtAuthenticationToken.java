package com.dire.guard.autentication;

import com.dire.guard.Payload;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private Payload payload;

    public JwtAuthenticationToken(Payload payload) {
        super(null);
        this.payload = payload;
        // 由于只有认证信息没有具体的权限，则认为该Token没有被认证，需要进行认证流程
        super.setAuthenticated(false);
    }

    // 有认证权限职责，则认为该认证信息已经被认证，不用走认证流程
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Payload payload) {
        super(authorities);
        this.payload = payload;
        super.setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(Payload payload) {
        return new JwtAuthenticationToken(payload);
    }

    public static JwtAuthenticationToken authenticated(Payload payload, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(authorities, payload);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        if (this.payload != null) {
            payload.setCredentials(null);
        }
    }

    @Override
    public Object getCredentials() {
        return this.payload.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return this.payload.getPrincipal();
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
