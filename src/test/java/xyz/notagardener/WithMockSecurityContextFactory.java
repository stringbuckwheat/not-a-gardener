package xyz.notagardener;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import xyz.notagardener.authentication.model.UserPrincipal;

import java.util.Collections;

public class WithMockSecurityContextFactory implements WithSecurityContextFactory<MockGardener> {
    @Override
    public SecurityContext createSecurityContext(MockGardener annotation) {
        UserPrincipal user = new UserPrincipal(annotation.id(), annotation.name());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,
                null,
                Collections.singleton(new SimpleGrantedAuthority("USER")));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);

        return context;
    }
}
