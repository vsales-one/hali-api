package com.hali.service;

import com.hali.domain.User;
import java.util.Optional;
public interface UserServiceExtension {
    Optional<User> loadUserByUID(String uid);
    public User registerFirebaseUser(String fullName, String uid, String email);
}

