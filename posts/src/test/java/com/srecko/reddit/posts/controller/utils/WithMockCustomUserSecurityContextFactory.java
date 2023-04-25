package com.srecko.reddit.posts.controller.utils;
/*

import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    for (String authority : customUser.authorities()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(authority));
    }
    User user = new User();
    user.setUsername(customUser.username());
    UserMediator userMediator = new UserMediator(user);
    Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(userMediator,
        "iloveyou", grantedAuthorities);
    context.setAuthentication(authentication);
    return context;
  }
}*/
