package com.hali.service;

import com.hali.security.jwt.FirebaseTokenHolder;

public interface FirebaseService {

    FirebaseTokenHolder parseToken(String idToken);

}
