package com.hali.service.impl;

import com.hali.domain.Authority;
import com.hali.domain.User;
import com.hali.repository.AuthorityRepository;
import com.hali.repository.UserRepository;
import com.hali.security.AuthoritiesConstants;
import com.hali.service.UserServiceExtension;
import com.hali.service.util.RandomUserNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class UserServiceExtensionImpl implements UserServiceExtension {

    private final Logger log = LoggerFactory.getLogger(UserServiceExtensionImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserServiceExtensionImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Optional<User> loadUserByUID(String uid) {
        return userRepository.findOneWithAuthoritiesByUid(uid);
    }

    public User registerFirebaseUser(String fullName, String uid, String email) {
        User newUser=userRepository.findOneByEmailIgnoreCase(email).orElse(null); // todo implement best practise handling of optional type
        if(newUser!=null){
            newUser.setUid(uid);
            userRepository.save(newUser);
        }else {
            newUser = new User();
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
            String encryptedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
            // firebase user gets initially a generated password
            newUser.setPassword(encryptedPassword);
            // firebase user gets initially a generated username
            RandomUserNameGenerator gen = new RandomUserNameGenerator(15, ThreadLocalRandom.current());
            newUser.setLogin(gen.nextString());

            String[] splitedFullname = fullName.split("\\s+");
            if (splitedFullname.length > 0) {
                newUser.setFirstName(splitedFullname[0]);
                if (splitedFullname.length > 1)
                    newUser.setLastName(splitedFullname[1]);
            }
            newUser.setEmail(email);
            newUser.setActivated(true);
            newUser.setAuthorities(authorities);
            newUser.setUid(uid);
            userRepository.save(newUser);
            log.debug("Created Information for User: {}", newUser);
        }
        return newUser;
    }
}
