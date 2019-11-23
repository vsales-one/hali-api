package com.hali.config.firebase;

import com.hali.domain.User;
import com.hali.service.UserService;
import com.hali.service.exception.FirebaseUserNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    public static final String NAME="FirebaseAuthenticationProvider";

    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userService;

//    @Autowired
//    private  UserService userService;

	public boolean supports(Class<?> authentication) {
		return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		FirebaseAuthenticationToken authenticationToken = (FirebaseAuthenticationToken) authentication;
//        User details= userService.loadUserByUID(((FirebaseTokenHolder)authenticationToken.getCredentials()).getUid()).orElse(null);
//		if (details == null) {
//                details=userService.registerFirebaseUser(
//                    ((FirebaseTokenHolder)authenticationToken.getCredentials()).getName(),
//                    ((FirebaseTokenHolder)authenticationToken.getCredentials()).getUid(),
//                    ((FirebaseTokenHolder)authenticationToken.getCredentials()).getEmail());
//        }
//        authenticationToken = new FirebaseAuthenticationToken( details , authentication.getCredentials(),
//           details.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//                .collect(Collectors.toList()));

        UserDetails details = userService.loadUserByUsername(((FirebaseTokenHolder)authenticationToken.getCredentials()).getEmail());
        if (details == null) {
            throw new FirebaseUserNotExistsException();
        }

        authenticationToken = new FirebaseAuthenticationToken(details, authentication.getCredentials(),
            details.getAuthorities());

        return authenticationToken;
	}

}
